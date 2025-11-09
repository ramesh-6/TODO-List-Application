package authservice.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserPrincipal Tests")
class UserPrincipalTest {

    private User testUser;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password123", "test@example.com");
        userPrincipal = new UserPrincipal(testUser);
    }

    @Test
    @DisplayName("getUsername - Returns Correct Username")
    void getUsername_ReturnsCorrectUsername() {
        assertThat(userPrincipal.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("getPassword - Returns Correct Password")
    void getPassword_ReturnsCorrectPassword() {
        assertThat(userPrincipal.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("getAuthorities - Returns USER Authority")
    void getAuthorities_ReturnsUserAuthority() {
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("USER");
    }

    @Test
    @DisplayName("isAccountNonExpired - Returns True")
    void isAccountNonExpired_ReturnsTrue() {
        assertThat(userPrincipal.isAccountNonExpired()).isTrue();
    }

    @Test
    @DisplayName("isAccountNonLocked - Returns True")
    void isAccountNonLocked_ReturnsTrue() {
        assertThat(userPrincipal.isAccountNonLocked()).isTrue();
    }

    @Test
    @DisplayName("isCredentialsNonExpired - Returns True")
    void isCredentialsNonExpired_ReturnsTrue() {
        assertThat(userPrincipal.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("isEnabled - Returns True")
    void isEnabled_ReturnsTrue() {
        assertThat(userPrincipal.isEnabled()).isTrue();
    }
}
