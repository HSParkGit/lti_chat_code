package kr.lineedu.lms.config.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import kr.lineedu.lms.feature.user.domain.User;
import kr.lineedu.lms.feature.user.domain.UserRepository;
import kr.lineedu.lms.global.enums.State;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public JwtPrincipal loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepository.findByStateAndLoginId(State.ACTIVE.getNumber(), username)
            .orElseThrow(() -> new UsernameNotFoundException("Email : " + username + " was not found"));
        return createUserDetails(user, null);
    }

    public JwtPrincipal checkUserByUserName(String userName, Claims claims) {
        User user = userRepository.findByStateAndLoginId(State.ACTIVE.getNumber(), userName)
            .orElseThrow(() -> new UsernameNotFoundException("Email : " + userName + " was not found"));
        return createUserDetails(user, claims);
    }

    private JwtPrincipal createUserDetails(User user, Claims claims) {
        String role = claims.get("role", String.class);

        return JwtPrincipal.builder()
            .username(user.getName())
            .userId(user.getId())
            .lmsUserId(user.getLmsUserId())
            .email(user.getEmail())
            .password(user.getPassword())
            .roles(getGrantedAuthorities(role))
            .build();
    }
    public JwtPrincipal initUserDetails(User user, String roleName) {

        return JwtPrincipal.builder()
            .username(user.getName())
            .email(user.getEmail())
            .password(user.getPassword())
            .roles(getGrantedAuthorities(roleName))
            .build();
    }

    private static List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}
