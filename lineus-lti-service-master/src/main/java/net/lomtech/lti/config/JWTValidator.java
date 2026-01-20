/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nimbusds.jose.JWSAlgorithm
 *  com.nimbusds.jose.JWSHeader
 *  com.nimbusds.jose.jwk.source.JWKSource
 *  com.nimbusds.jose.jwk.source.RemoteJWKSet
 *  com.nimbusds.jose.proc.JWSKeySelector
 *  com.nimbusds.jose.proc.JWSVerificationKeySelector
 *  com.nimbusds.jose.proc.SecurityContext
 *  com.nimbusds.jose.util.DefaultResourceRetriever
 *  com.nimbusds.jose.util.ResourceRetriever
 *  com.nimbusds.jwt.JWTClaimsSet
 *  com.nimbusds.jwt.SignedJWT
 *  com.nimbusds.jwt.proc.DefaultJWTProcessor
 *  net.lomtech.helloworld.lti.config.JWTValidator
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.lomtech.lti.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.URL;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTValidator {
    private static final Logger logger = LoggerFactory.getLogger(JWTValidator.class);

    public static JWTClaimsSet validateJWT(String jwt, String jwksUri, String issuer, String clientId) throws Exception {
        DefaultJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        DefaultResourceRetriever resourceRetriever = new DefaultResourceRetriever(5000, 5000);
        RemoteJWKSet keySource = new RemoteJWKSet(new URL(jwksUri), (ResourceRetriever)resourceRetriever);
        SignedJWT signedJWT = SignedJWT.parse((String)jwt);
        JWSHeader header = signedJWT.getHeader();
        JWSAlgorithm algorithm = header.getAlgorithm();
        logger.info("JWT Header: {}", (Object)header.toString());
        logger.info("JWT Payload: {}", (Object)signedJWT.getJWTClaimsSet().toString());
        HashSet<JWSAlgorithm> acceptedAlgs = new HashSet<JWSAlgorithm>();
        acceptedAlgs.add(JWSAlgorithm.RS256);
        if (!acceptedAlgs.contains(algorithm)) {
            throw new Exception("Unsupported algorithm: " + String.valueOf(algorithm));
        }
        JWSVerificationKeySelector keySelector = new JWSVerificationKeySelector(algorithm, (JWKSource)keySource);
        jwtProcessor.setJWSKeySelector((JWSKeySelector)keySelector);
        SecurityContext ctx = null;
        JWTClaimsSet claimsSet = jwtProcessor.process(signedJWT, ctx);
        if (!issuer.equals(claimsSet.getIssuer())) {
            throw new Exception("Invalid issuer");
        }
        if (!clientId.equals(claimsSet.getAudience().get(0))) {
            throw new Exception("Invalid client ID");
        }
        return claimsSet;
    }
}

