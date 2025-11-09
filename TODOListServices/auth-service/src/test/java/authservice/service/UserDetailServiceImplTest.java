package authservice.service;

import authservice.client.UserServiceClient;
import authservice.entity.User;
import authservice.entity.UserPrincipal;
import authservice.exception.AuthenticationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailServiceImpl Tests")
class UserDetailServiceImplTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password123", "test@example.com");
    }

    @Test
    @DisplayName("loadUserByUsername - Success")
    void loadUserByUsername_Success() {
        when(userServiceClient.findByUsername("testuser"))
                .thenReturn(ResponseEntity.ok(testUser));

        UserDetails userDetails = userDetailService.loadUserByUsername("testuser");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails).isInstanceOf(UserPrincipal.class);
        verify(userServiceClient).findByUsername("testuser");
    }

    @Test
    @DisplayName("loadUserByUsername - User Not Found")
    void loadUserByUsername_UserNotFound() {
        when(userServiceClient.findByUsername("nonexistent"))
                .thenReturn(ResponseEntity.ok(null));

        assertThatThrownBy(() -> userDetailService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Failed to load user: nonexistent");

        verify(userServiceClient).findByUsername("nonexistent");
    }


    @Test
    @DisplayName("loadUserByUsername - Feign Client Exception")
    void loadUserByUsername_FeignClientException() {
        when(userServiceClient.findByUsername("testuser"))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThatThrownBy(() -> userDetailService.loadUserByUsername("testuser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Failed to load user: testuser");

        verify(userServiceClient).findByUsername("testuser");
    }

    @Test
    @DisplayName("verify - Success")
    void verify_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userServiceClient.findByUsername("testuser"))
                .thenReturn(ResponseEntity.ok(testUser));
        when(jwtService.generateToken("testuser", 1L))
                .thenReturn("mock.jwt.token");

        String token = userDetailService.verify(testUser);

        assertThat(token).isEqualTo("mock.jwt.token");
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userServiceClient).findByUsername("testuser");
        verify(jwtService).generateToken("testuser", 1L);
    }

    @Test
    @DisplayName("verify - Authentication Failed")
    void verify_AuthenticationFailed() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        assertThatThrownBy(() -> userDetailService.verify(testUser))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessageContaining("Authentication failed for user: testuser");

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    @DisplayName("verify - Bad Credentials")
    void verify_BadCredentials() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> userDetailService.verify(testUser))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessageContaining("Authentication failed for user: testuser");

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    @DisplayName("verify - Feign Client Fails After Authentication")
    void verify_FeignClientFailsAfterAuth() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userServiceClient.findByUsername("testuser"))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThatThrownBy(() -> userDetailService.verify(testUser))
                .isInstanceOf(AuthenticationFailedException.class);

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userServiceClient).findByUsername("testuser");
    }
}
