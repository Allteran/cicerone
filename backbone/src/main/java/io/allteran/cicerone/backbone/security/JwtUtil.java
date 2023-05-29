package io.allteran.cicerone.backbone.security;

import io.allteran.cicerone.backbone.domain.Role;
import io.allteran.cicerone.backbone.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration}")
    private String EXPIRATION_TIME;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token) {
        return getClaimsFromToken(token)
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        //here we put im claims list of Role.id
        List<String> authorities = user.getRoles().stream()
                .map(Role::getAuthority)
                .toList();
        claims.put("roles", authorities);

        long expirationSeconds = Long.parseLong(EXPIRATION_TIME);
        Date creationDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + expirationSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .signWith(key)
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .compact();
    }

    public boolean validateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expDate = claims.getExpiration();
        return new Date().before(expDate);
    }
}
