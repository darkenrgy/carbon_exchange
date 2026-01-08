package rs.example.carbon_traders.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // master key
    private final String SECRET_KEY = "carbon_credit_secret_key_123";

    // Token validity (1 day)
    private final long JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 24;

        //  TOKEN GENERATION

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        // add role to token
        claims.put(
                "role",
                userDetails.getAuthorities().iterator().next().getAuthority()
        );

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME)
                )
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //  TOKEN VALIDATION

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token)
        );
    }


    //  CLAIM EXTRACTION


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

