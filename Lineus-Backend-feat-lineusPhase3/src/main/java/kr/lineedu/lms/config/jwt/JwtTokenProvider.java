package kr.lineedu.lms.config.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.lineedu.lms.config.properties.AppProperties;
import kr.lineedu.lms.feature.user.api.feign.AccountClient;
import kr.lineedu.lms.feature.user.domain.User;
import kr.lineedu.lms.feature.user.domain.UserRepository;
import kr.lineedu.lms.global.enums.Role;
import lombok.extern.slf4j.Slf4j;

// token 생성과 유효 검사를 진행하는 클래스입니다
@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private final PrincipalDetailsService principalDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_SERVICE = "refreshTokenService";
    private final String secret;
    private final long accessTokenTime;
    private final long refreshTokenTime;
    private final String issuer;
    private Key key;
    private final UserRepository userRepository;
    private final AccountClient accountClient;

    public JwtTokenProvider(
        AppProperties appProperties,
        PrincipalDetailsService principalDetailsService,
        RedisTemplate redisTemplate,
        UserRepository userRepository,
        AccountClient accountClient
    ) {
        this.secret = appProperties.jwt().secret();
        this.accessTokenTime = appProperties.jwt().accessTokenTime() * 1000 * 60; // Dev a week, Prod 6 hours
        this.refreshTokenTime = appProperties.jwt().refreshTokenTime() * 1000 * 14; // 2주
        this.principalDetailsService = principalDetailsService;
        this.issuer = appProperties.jwt().issuer();
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.accountClient = accountClient;
    }

    // initializingBean을 통해서 afterPropretiesSet을 구현한 이유
    // 주입을 받은 후에 secret 값을 base64 decode 해서 key 변수에 할당하기 위해서 입니다
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes;
        try {
            // prefer base64 secrets for compatibility with existing deployments
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException decodeEx) {
            // fall back to raw bytes if the secret is not base64 encoded
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 64) {
            // HS512 requires a key size >= 512 bits; derive a deterministic 512-bit key
            keyBytes = derive512BitKey(keyBytes);
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] derive512BitKey(byte[] source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(source); // 64 bytes (512 bits)
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 algorithm not available", e);
        }
    }

    // Authentication 정보를 이용해서 AccessToken을 생성하는 메소드
    public String createAccessToken(User user, Role roleName, String userNumber) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + accessTokenTime);

        // AccessToken 생성
        return Jwts.builder()
            .setSubject(user.getEmail()) //userEmail로  user-> userDetails 추출용
            .setIssuedAt(now) //발행시간
            .setExpiration(expireTime)
            .claim("iss", issuer)  //발급자
            .claim("division", user.getDivision())
            .claim("subDivision", user.getSubDivision())
            .claim("loginId", userNumber) // userNumber로 로그인 = User테이블의 loginId
            .claim("name", user.getName())
            .claim("role", roleName)
            .claim("email", user.getEmail())
            .claim("mobile", user.getMobile())
            .claim("birth", user.getBirth())
            .signWith(key, SignatureAlgorithm.HS512) // 이걸로 잠금
            .compact();
    }

    private String createNewRefreshToken(User user, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenTime);

        // refreshToken 생성
        return Jwts.builder()
            .setSubject(REFRESH_TOKEN_SERVICE)
            .claim("loginId", user.getLoginId())
            .claim("role", role)
            .setIssuedAt(now)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    // token을 받아서 클레임을 만들고 이걸로 user 객체를 만들어서 최종적으로 authentication 객체를 반환하는 메소드입니다
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        //user -> userDetails
        JwtPrincipal userDetails = principalDetailsService.checkUserByUserName(
            claims.get("loginId", String.class),
            claims);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // token 검증하는 메소드
    public JwtCode validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException exception) {
            //
            return JwtCode.DENIED;
        } catch (ExpiredJwtException e) {
            // 이 값이면 refresh 토큰 검증
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }

    // 처음에 로그인했을 때 발급하는 용도
    @Transactional
    public String createRefreshToken(User user, Role role) {

        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String newRefreshToken = createNewRefreshToken(user, role);

        //refresh Token redis에 저장
        vop.set(String.valueOf(newRefreshToken), "refreshtoken");
        return newRefreshToken;
    }

    public Claims parseToken(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED,
    }

    public static boolean isAdminRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Optional<Role> userRole = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(Role::fromString)
                    .filter(role -> role == Role.ADMIN)
                    .findFirst();
            return userRole.isPresent();
        }
        return false;
    }

    public static boolean isStudentRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Optional<Role> userRole = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(Role::fromString)
                    .filter(role -> role == Role.STUDENT)
                    .findFirst();
            return userRole.isPresent();
        }
        return false;
    }
}
