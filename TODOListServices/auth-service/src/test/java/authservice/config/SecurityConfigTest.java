package authservice.config;

import authservice.client.UserServiceClient;
import authservice.entity.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Security Configuration Tests")
class SecurityConfigTest {

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
    @DisplayName("Public Endpoint - /auth/register - Should Allow Without Authentication")
    void publicEndpoint_Register_AllowsAccess() throws Exception {
        User user = new User(1L, "testuser", "password123", "test@example.com");

        when(userServiceClient.createUser(any(User.class)))
                .thenReturn(ResponseEntity.status(201).body(user));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Public Endpoint - /auth/login - Should Allow Without Authentication")
    void publicEndpoint_Login_AllowsAccess() throws Exception {
        User user = new User(1L, "testuser", "password123", "test@example.com");
        String mockToken = "mock.jwt.token";

        when(userDetailService.verify(any(User.class))).thenReturn(mockToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public Endpoint - /auth/validate - Should Allow Without Authentication")
    void publicEndpoint_Validate_AllowsAccess() throws Exception {
        String token = "mock.jwt.token";

        when(jwtService.extractUserName(token)).thenReturn("testuser");
        when(jwtService.extractUserId(token)).thenReturn(1L);
        when(jwtService.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public Endpoint - /auth/validate/header - Should Allow Without Authentication")
    void publicEndpoint_ValidateByHeader_AllowsAccess() throws Exception {
        String mockToken = "mock.jwt.token";

        when(jwtService.validateAndExtract(mockToken))
                .thenReturn(Map.of("username", "testuser", "userId", 1L));

        mockMvc.perform(post("/auth/validate/header")
                        .header("Authorization", "Bearer " + mockToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CSRF - Should Be Disabled for Stateless API")
    void csrf_ShouldBeDisabled() throws Exception {
        User user = new User(1L, "testuser", "password123", "test@example.com");

        when(userServiceClient.createUser(any(User.class)))
                .thenReturn(ResponseEntity.status(201).body(user));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Session - Should Use Stateless Session Management")
    void session_ShouldBeStateless() throws Exception {
        User user = new User(1L, "testuser", "password123", "test@example.com");

        when(userServiceClient.createUser(any(User.class)))
                .thenReturn(ResponseEntity.status(201).body(user));

        var result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        var cookies = result.getResponse().getCookies();
        boolean hasSessionCookie = false;
        for (var cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                hasSessionCookie = true;
                break;
            }
        }

        assert !hasSessionCookie : "Session should not be created for stateless API";
    }
}
