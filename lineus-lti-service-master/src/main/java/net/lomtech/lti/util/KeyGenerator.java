package net.lomtech.lti.util;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Utility class for generating RSA key pairs for LTI 1.3.
 * 
 * Usage:
 * 1. Generate a key pair once
 * 2. Store the private key securely (environment variable, key store, etc.)
 * 3. Expose the public key via JWKS endpoint
 */
public class KeyGenerator {
    private static final Logger log = LoggerFactory.getLogger(KeyGenerator.class);
    
    /**
     * Generates a new RSA key pair for LTI 1.3.
     * 
     * @return JWKSet containing the public key
     * @throws Exception if key generation fails
     */
    public static JWKSet generateKeyPair() throws Exception {
        log.info("Generating RSA key pair for LTI 1.3...");
        
        // Generate RSA key pair (2048 bits minimum for LTI 1.3)
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        
        // Create RSA JWK from the key pair
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
            .privateKey(keyPair.getPrivate())
            .keyUse(KeyUse.SIGNATURE)
            .keyID(UUID.randomUUID().toString())
            .build();
        
        JWKSet jwkSet = new JWKSet(rsaKey);
        
        log.info("Key pair generated successfully. Key ID: {}", rsaKey.getKeyID());
        log.warn("IMPORTANT: Store the private key securely! Do not expose it publicly.");
        
        return jwkSet;
    }
    
    /**
     * Creates a JWKSet from a public key only (for JWKS endpoint).
     * 
     * @param publicKey The RSA public key
     * @return JWKSet containing only the public key
     */
    public static JWKSet createPublicJWKSet(RSAPublicKey publicKey) {
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .keyUse(KeyUse.SIGNATURE)
            .keyID("lineus-lti-key-1")
            .build();
        
        return new JWKSet(rsaKey);
    }
}

