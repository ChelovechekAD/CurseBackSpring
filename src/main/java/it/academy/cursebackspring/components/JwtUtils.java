package it.academy.cursebackspring.components;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import it.academy.cursebackspring.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${spring.application.security.jwt.access-key.value}")
    private String jwtAccessSecret;

    @Value("${spring.application.security.jwt.refresh-key.value}")
    private String jwtRefreshSecret;

    @Value("${spring.application.security.jwt.access-key.expiration-time}")
    private Integer jwtAccessExpiration;

    @Value("${spring.application.security.jwt.refresh-key.expiration-time}")
    private Integer jwtRefreshExpiration;


    /**
     * Token validation
     *
     * @param token token
     * @return true, if token valid
     */
    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("WT signature does not match locally computed signature.");
        }

        return false;
    }

    /**
     * Generate access token
     *
     * @param user user object
     * @return token
     */
    public String generateAccessToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(jwtAccessExpiration)
                .atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(accessExpirationInstant))
                .signWith(getAccessSigningKey())
                .claim("id", user.getId())
                .claim("roles", user.getAuthorities())
                .compact();
    }

    /**
     * Generate refresh token
     *
     * @param user user object
     * @return token
     */

    public String generateRefreshToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusMinutes(jwtRefreshExpiration)
                .atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(refreshExpirationInstant))
                .signWith(getRefreshSigningKey())
                .compact();
    }

    /**
     * Validate access token
     *
     * @param token token
     * @return true, if token valid
     */
    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessSigningKey());
    }

    /**
     * Validate refresh token
     *
     * @param token token
     * @return true, if token valid
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshSigningKey());
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract claim from access token
     *
     * @param token token
     * @return extracted claims
     */
    public Claims getAccessClaims(String token) {
        return getClaims(token, getAccessSigningKey());
    }

    /**
     * Extract claim from refresh token
     *
     * @param token token
     * @return extracted claims
     */
    public Claims getRefreshClaims(String token) {
        return getClaims(token, getRefreshSigningKey());
    }

    /**
     * Get refresh signing key
     *
     * @return key
     */
    private Key getRefreshSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtRefreshSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Get access signing key
     *
     * @return key
     */
    private Key getAccessSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtAccessSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
