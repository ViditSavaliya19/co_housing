package com.example.co_housing.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String secret = "2l6EnOPICvttu2HUtkUtD0glq9P6NhIqqWUcGowe06h4IoGLLdPy0psDZJcp0uSFf4E4wiilcTAsvOjylZEZ2JVv4vH4RxEiB7tOeLWuMEmoQzuaoy6DJGzgT1uUrBsvGmqKSkRXryMm1TjtSmf9gePDxhyB60KIu0IqzmndUX8XYDSSkJdEtuYnLLa0GB1YOqVbotPMYLCES1O0IeRnTxF7hbm8sQe35z0wILZ2dZ91L1HJxAqjIk6QGdW1bcMO9B3IEv3Ta1uMDIMIpMoQBfsD670TQxfB";

    private final long EXPIRATION_MS = 86400000; // 24h

    public String generateToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim using a resolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
