package cloudgateway.integration;

import cloudgateway.GatewayApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false"
})
@DisplayName("Gateway Routing Integration Tests")
class GatewayRoutingIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Auth Service Route - Should Return 503 (Service Unavailable)")
    void authServiceRoute_ShouldRouteCorrectly() {
        webTestClient.get()
                .uri("/auth/health")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("User Service Route - Should Apply JWT Filter and Block")
    void userServiceRoute_ShouldApplyJwtFilter() {
        webTestClient.get()
                .uri("/users/all")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Task Service Route - Should Apply JWT Filter and Block")
    void taskServiceRoute_ShouldApplyJwtFilter() {
        webTestClient.get()
                .uri("/tasks/all")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Unknown Route - Should Return 404")
    void unknownRoute_ShouldReturn404() {
        webTestClient.get()
                .uri("/unknown/endpoint")
                .exchange()
                .expectStatus().isNotFound();
    }
}
