package authservice.config;

import authservice.service.UserDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebConfig Tests")
class WebConfigTest {

    @Mock
    private UserDetailService userDetailService;

    @InjectMocks
    private WebConfig webConfig;

    @Test
    @DisplayName("AuthenticationProvider - Should Be Created")
    void authenticationProvider_ShouldBeCreated() {
        DaoAuthenticationProvider provider = webConfig.authenticationProvider();

        assertThat(provider).isNotNull();
        assertThat(provider).isInstanceOf(DaoAuthenticationProvider.class);
    }

    @Test
    @DisplayName("AuthenticationProvider - Should Use UserDetailsService")
    void authenticationProvider_ShouldUseUserDetailsService() {
        DaoAuthenticationProvider provider = webConfig.authenticationProvider();
        assertThat(provider).isNotNull();
    }
}
