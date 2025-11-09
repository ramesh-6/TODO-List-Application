package authservice.controller;

import authservice.client.UserServiceClient;
import authservice.entity.User;
import authservice.service.JwtService;
import authservice.service.UserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceClient userServiceClient;

    @MockitoBean
    private UserDetailService userDetailService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password123", "test@example.com");
    }

    @Test
    @DisplayName("register - Success")
    void register_Success() throws Exception {
        when(userServiceClient.createUser(any(User.class)))
                .thenReturn(ResponseEntity.status(201).body(testUser));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userServiceClient).createUser(any(User.class));
    }

    @Test
    @DisplayName("login - Success")
    void login_Success() throws Exception {
        String mockToken = "mock.jwt.token";
        when(userDetailService.verify(any(User.class))).thenReturn(mockToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string(mockToken));

        verify(userDetailService).verify(any(User.class));
    }

    @Test
    @DisplayName("login - Invalid Credentials")
    void login_InvalidCredentials() throws Exception {
        when(userDetailService.verify(any(User.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isUnauthorized());

        verify(userDetailService).verify(any(User.class));
    }

    @Test
    @DisplayName("validate - Success")
    void validate_Success() throws Exception {
        String mockToken = "mock.jwt.token";
        when(jwtService.extractUserName(mockToken)).thenReturn("testuser");
        when(jwtService.extractUserId(mockToken)).thenReturn(1L);
        when(jwtService.validateToken(eq(mockToken), any())).thenReturn(true);

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.userId").value(1));

        verify(jwtService).extractUserName(mockToken);
        verify(jwtService).extractUserId(mockToken);
        verify(jwtService).validateToken(eq(mockToken), any());
    }

    @Test
    @DisplayName("validate - Invalid Token")
    void validate_InvalidToken() throws Exception {
        String invalidToken = "invalid.token";
        when(jwtService.extractUserName(invalidToken))
                .thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidToken))
                .andExpect(status().isUnauthorized());

        verify(jwtService).extractUserName(invalidToken);
    }

    @Test
    @DisplayName("validate - Expired Token")
    void validate_ExpiredToken() throws Exception {
        String expiredToken = "expired.jwt.token";
        when(jwtService.extractUserName(expiredToken)).thenReturn("testuser");
        when(jwtService.extractUserId(expiredToken)).thenReturn(1L);
        when(jwtService.validateToken(eq(expiredToken), any())).thenReturn(false);

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expiredToken))
                .andExpect(status().isUnauthorized());

        verify(jwtService).validateToken(eq(expiredToken), any());
    }

    @Test
    @DisplayName("validateByHeader - Success")
    void validateByHeader_Success() throws Exception {
        String mockToken = "mock.jwt.token";
        String authHeader = "Bearer " + mockToken;

        when(jwtService.validateAndExtract(mockToken))
                .thenReturn(Map.of("username", "testuser", "userId", 1L));

        mockMvc.perform(post("/auth/validate/header")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk());

        verify(jwtService).validateAndExtract(mockToken);
    }

    @Test
    @DisplayName("validateByHeader - Missing Authorization Header")
    void validateByHeader_MissingHeader() throws Exception {
        mockMvc.perform(post("/auth/validate/header"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authorization header is required"));

        verify(jwtService, never()).validateAndExtract(any());
    }

    @Test
    @DisplayName("validateByHeader - Invalid Authorization Header Format")
    void validateByHeader_InvalidFormat() throws Exception {
        mockMvc.perform(post("/auth/validate/header")
                        .header("Authorization", "InvalidFormat token"))
                .andExpect(status().isUnauthorized());

        verify(jwtService, never()).validateAndExtract(any());
    }

    @Test
    @DisplayName("validateByHeader - Invalid Token")
    void validateByHeader_InvalidToken() throws Exception {
        String mockToken = "invalid.token";
        String authHeader = "Bearer " + mockToken;

        when(jwtService.validateAndExtract(mockToken))
                .thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/auth/validate/header")
                        .header("Authorization", authHeader))
                .andExpect(status().isUnauthorized());

        verify(jwtService).validateAndExtract(mockToken);
    }
}
