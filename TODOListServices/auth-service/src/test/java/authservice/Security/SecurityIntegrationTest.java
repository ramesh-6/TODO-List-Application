package authservice.Security;

import authservice.client.UserServiceClient;
import authservice.entity.User;
import authservice.entity.UserPrincipal;
import authservice.exception.AuthenticationFailedException;
import authservice.service.JwtService;
import authservice.service.UserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Security Integration Tests")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceClient userServiceClient;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailService userDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Integration - Complete Registration Flow")
    void integration_RegistrationFlow_Success() throws Exception {
        User user = new User(null, "newuser", "securePass123", "newuser@test.com");
        User savedUser = new User(1L, "newuser", "securePass123", "newuser@test.com");

        when(userServiceClient.createUser(any(User.class)))
                .thenReturn(ResponseEntity.status(201).body(savedUser));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"));
    }

    @Test
    @DisplayName("Integration - Login Success")
    void integration_LoginSuccess() throws Exception {
        User loginUser = new User(1L, "testuser", "password123", "test@example.com");
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";

        when(userDetailService.verify(any(User.class))).thenReturn(mockToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(content().string(mockToken));
    }

    @Test
    @DisplayName("Integration - Token Validation Success")
    void integration_TokenValidationSuccess() throws Exception {
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
        User dbUser = new User(1L, "testuser", "password123", "test@example.com");

        when(jwtService.extractUserName(mockToken)).thenReturn("testuser");
        when(jwtService.extractUserId(mockToken)).thenReturn(1L);
        when(userServiceClient.findByUsername("testuser"))
                .thenReturn(ResponseEntity.ok(dbUser));
        when(jwtService.validateToken(anyString(), any(UserPrincipal.class))).thenReturn(true);

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("Integration - Token Validation via Header Success")
    void integration_HeaderValidationSuccess() throws Exception {
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";

        when(jwtService.validateAndExtract(mockToken))
                .thenReturn(Map.of("username", "testuser", "userId", 1L));

        mockMvc.perform(post("/auth/validate/header")
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Integration - Login Failure")
    void integration_LoginFailure() throws Exception {
        User loginUser = new User(1L, "testuser", "wrongpassword", "test@example.com");

        when(userDetailService.verify(any(User.class)))
                .thenThrow(new AuthenticationFailedException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Integration - Invalid Token")
    void integration_InvalidToken() throws Exception {
        String invalidToken = "invalid.token.here";

        when(jwtService.extractUserName(invalidToken))
                .thenThrow(new RuntimeException("Invalid token format"));

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Integration - Missing Authorization Header")
    void integration_MissingAuthHeader() throws Exception {
        mockMvc.perform(post("/auth/validate/header"))
                .andExpect(status().isUnauthorized());
    }
}
