package com.example.expensetrackerassignment.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class JwtUtils {

    private static final String SECRET_KEY = "secret";

    // Generate a token with roles
    public static String generateToken(String email, List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(email)
                .withClaim("roles", roles)  // Add roles as a claim
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .sign(algorithm);
    }

    // Validate token and return email and roles
    public static TokenData validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            String email = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);  // Get roles from the token

            return new TokenData(email, roles);  // Return both email and roles
        } catch (Exception e) {
            return null;  // If token is invalid, return null
        }
    }

    // TokenData class to hold email and roles
    public static class TokenData {
        private String email;
        private List<String> roles;

        public TokenData(String email, List<String> roles) {
            this.email = email;
            this.roles = roles;
        }

        public String getEmail() {
            return email;
        }

        public List<String> getRoles() {
            return roles;
        }
    }
}
