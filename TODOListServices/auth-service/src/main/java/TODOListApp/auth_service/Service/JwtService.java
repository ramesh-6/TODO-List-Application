package TODOListApp.auth_service.Service;

import TODOListApp.auth_service.Entity.User;
import TODOListApp.auth_service.Entity.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final String secretKey;

    private final long expirationTimeInSeconds;

    public JwtService(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") Long expirationTimeInSeconds) {
        this.secretKey = secretKey;
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }

    public String generateToken(String username, Long userId) {
        logger.info("Generating JWT token for user: {} with ID: {}", username, userId);
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userId", userId);
        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeInSeconds * 1000))
                .and()
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.debug("Generated Token: {}", token);
        return token;
    }

    private SecretKey getKey() {
        logger.debug("Decoding secret key for JWT signing");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        logger.info("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        logger.info("Extracting user ID from token");
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logger.info("Extracting all claims from token");
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        boolean isValid = userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
        logger.info("Token validation result for user {}: {}", userName, isValid);
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        logger.info("Token expired: {}", expired);
        return expired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Map<String, Object> validateAndExtract(String token) {
        logger.info("Validating and extracting data from token");
        Map<String, Object> response = new HashMap<>();
        String username = extractUserName(token);
        Long userId = extractUserId(token);

        logger.debug("Extracted from token - Username: {}, UserId: {}", username, userId);

        UserDetails userDetails = new UserPrincipal(new User(userId, username, "", ""));
        boolean isValid = validateToken(token, userDetails);

        if (!isValid) {
            logger.error("Token is invalid or expired for user: {}", username);
            throw new RuntimeException("Invalid or expired token");
        }

        response.put("username", username);
        response.put("userId", userId);
        logger.info("Token successfully validated for user: {}", username);
        return response;
    }

}