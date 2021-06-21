package com.decoo.psa.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.decoo.psa.domain.ApiKey;

import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    public static class JwtApiKey {

        public JwtApiKey(String apiKey, Long userId) {
            this.apiKey = apiKey;
            this.userId = userId;
        }

        public String apiKey;
        public Long userId;
    }

    public static String createJwtByUser(ApiKey apiKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("api_key", apiKey.getApiKey());
        map.put("user_id", apiKey.getUserId());
        return JWT.create().withClaim("key", map).sign(Algorithm.HMAC256(apiKey.getApiSecret()));
    }

    public static String getSecretKeyByToken(String token) {
        JwtApiKey jwtApiKey = getJwtApiKeyByJwtToken(token);
        return jwtApiKey == null ? null : jwtApiKey.apiKey;
    }

    public static JwtApiKey getJwtApiKeyByJwtToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Claim claim = decodedJWT.getClaim("key");
        if (claim != null) {
            Map<String, Object> map = claim.asMap();
            if (map != null) {
                return new JwtApiKey((String)map.get("api_key"),
                        ((Integer)map.get("user_id")).longValue());
            }
        }
        return null;
    }

    public static boolean verifyToken(String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
