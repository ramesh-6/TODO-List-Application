package authservice.service;

import authservice.entity.User;
import authservice.entity.UserPrincipal;
import authservice.exception.InvalidJwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private String secretKey;
    private Long expirationTime;

    @BeforeEach
    void setUp() {
        secretKey = "MySecretKeyForJWTTokenGenerationAndValidation1234567890ABCDEF";
        expirationTime = 3600L;
        jwtService = new JwtService(secretKey, expirationTime);
    }

    @Test
    @DisplayName("generateToken - Success")
    void generateToken_Success() {
        String token = jwtService.generateToken("testuser", 1L);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("extractUserName - Valid Token")
    void extractUserName_ValidToken() {
        String token = jwtService.generateToken("testuser", 1L);

        String username = jwtService.extractUserName(token);

        assertThat(username).isEqualTo("testuser");
    }

    @Test
    @DisplayName("extractUserId - Valid Token")
    void extractUserId_ValidToken() {
        String token = jwtService.generateToken("testuser", 100L);

        Long userId = jwtService.extractUserId(token);

        assertThat(userId).isEqualTo(100L);
    }

    @Test
    @DisplayName("validateToken - Valid Token")
    void validateToken_ValidToken() {
        String token = jwtService.generateToken("testuser", 1L);
        User user = new User(1L, "testuser", "password", "test@example.com");
        UserDetails userDetails = new UserPrincipal(user);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("validateToken - Wrong Username")
    void validateToken_WrongUsername() {
        String token = jwtService.generateToken("testuser", 1L);
        User user = new User(1L, "wronguser", "password", "test@example.com");
        UserDetails userDetails = new UserPrincipal(user);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("validateToken - Expired Token")
    void validateToken_ExpiredToken() throws InterruptedException {
        JwtService shortLivedService = new JwtService(secretKey, 1L);
        String token = shortLivedService.generateToken("testuser", 1L);
        Thread.sleep(2000);
        User user = new User(1L, "testuser", "password", "test@example.com");
        UserDetails userDetails = new UserPrincipal(user);
        boolean isValid = shortLivedService.validateToken(token, userDetails);
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("validateAndExtract - Valid Token")
    void validateAndExtract_ValidToken() {
        String token = jwtService.generateToken("testuser", 1L);

        Map<String, Object> result = jwtService.validateAndExtract(token);

        assertThat(result).containsEntry("username", "testuser");
        assertThat(result).containsEntry("userId", 1L);
    }

    @Test
    @DisplayName("validateAndExtract - Expired Token Throws Exception")
    void validateAndExtract_ExpiredToken() throws InterruptedException {
        JwtService shortLivedService = new JwtService(secretKey, 1L);
        String token = shortLivedService.generateToken("testuser", 1L);

        Thread.sleep(2000);

        assertThatThrownBy(() -> shortLivedService.validateAndExtract(token))
                .isInstanceOf(InvalidJwtToken.class)
                .hasMessage("Invalid or expired token");
    }

    @Test
    @DisplayName("extractUserName - Invalid Token Format")
    void extractUserName_InvalidToken() {
        String invalidToken = "invalid.token.format";

        assertThatThrownBy(() -> jwtService.extractUserName(invalidToken))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("extractUserId - Invalid Token Format")
    void extractUserId_InvalidToken() {
        String invalidToken = "invalid.token.format";

        assertThatThrownBy(() -> jwtService.extractUserId(invalidToken))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("generateToken - With Special Characters")
    void generateToken_WithSpecialCharacters() {
        String token = jwtService.generateToken("test.user@example", 999L);

        String username = jwtService.extractUserName(token);
        Long userId = jwtService.extractUserId(token);

        assertThat(username).isEqualTo("test.user@example");
        assertThat(userId).isEqualTo(999L);
    }

    @Test
    @DisplayName("validateToken - Null UserDetails Returns False")
    void validateToken_NullUserDetails() {
        String token = jwtService.generateToken("testuser", 1L);

        boolean isValid = jwtService.validateToken(token, null);

        assertThat(isValid).isFalse();
    }
}
