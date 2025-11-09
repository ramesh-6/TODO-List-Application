package cloudgateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false"
})
@DisplayName("Gateway Application Context Tests")
class GatewayApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RouteLocator routeLocator;

    @Test
    @DisplayName("Application Context - Should Load")
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    @DisplayName("RouteLocator Bean - Should Be Available")
    void routeLocator_ShouldBeAvailable() {
        assertThat(routeLocator).isNotNull();
    }

    @Test
    @DisplayName("Routes - Should Be Configured")
    void routes_ShouldBeConfigured() {
        var routes = routeLocator.getRoutes().collectList().block();
        assertThat(routes).isNotEmpty();
    }
}
