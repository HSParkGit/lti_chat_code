package net.lomtech.lti.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Controller for serving JWKS (JSON Web Key Set) endpoint.
 * This endpoint provides the public key for LTI 1.3 tool authentication.
 * 
 * Note: For production, you should generate a key pair once and store it securely.
 * This implementation generates a key on startup for convenience.
 */
@RestController
public class JWKSController {
    private static final Logger log = LoggerFactory.getLogger(JWKSController.class);
    
    private JWKSet jwkSet;
    private KeyPair keyPair;
    
    @PostConstruct
    public void initialize() {
        try {
            log.info("Generating RSA key pair for LTI 1.3 JWKS...");
            
            // Generate RSA key pair (2048 bits minimum for LTI 1.3)
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            keyPair = keyGen.generateKeyPair();
            
            // Create RSA JWK from the public key
            RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .build();
            
            jwkSet = new JWKSet(rsaKey);
            
            log.info("RSA key pair generated successfully. Key ID: {}", rsaKey.getKeyID());
            log.warn("IMPORTANT: For production, generate keys once and store the private key securely!");
        } catch (Exception e) {
            log.error("Failed to generate RSA key pair", e);
            throw new RuntimeException("Failed to initialize JWKS", e);
        }
    }
    
    @GetMapping(value = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getJWKS() {
        log.info("JWKS requested");
        
        if (jwkSet == null) {
            log.error("JWKS not initialized");
            return ResponseEntity.internalServerError().build();
        }
        
        // Return only the public key (JWKS format)
        JWKSet publicJWKSet = new JWKSet(jwkSet.getKeys());
        Object jwksObject = publicJWKSet.toJSONObject();
        
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(jwksObject);
    }
    
    /**
     * Get the private key for signing requests (if needed).
     * In production, this should be stored securely and not exposed.
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }
}

