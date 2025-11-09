package cloudgateway.filter;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "gateway.secret=test-secret-123",
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false"
})
@DisplayName("JwtAuthFilter Reactive Tests")
class JwtAuthFilterTest {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    @DisplayName("Filter - Valid JWT Should Allow Request")
    void filter_WithValidJWT_ShouldAllowRequest() {
        stubFor(post(urlEqualTo("/auth/validate/header"))
                .willReturn(aResponse()
                        .withStatus(200)));

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid.token.here")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        var filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, ex -> Mono.empty());

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("Filter - Invalid JWT Should Reject Request")
    void filter_WithInvalidJWT_ShouldRejectRequest() {
        stubFor(post(urlEqualTo("/auth/validate/header"))
                .willReturn(aResponse()
                        .withStatus(401)));

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        var filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, ex -> Mono.empty());

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Filter - Missing Authorization Header Should Reject")
    void filter_WithoutAuthHeader_ShouldReject() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/users")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        var filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, ex -> Mono.empty());

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Filter - Gateway Token Should Bypass Auth")
    void filter_WithGatewayToken_ShouldBypass() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/users")
                .header("X-Gateway-Token", "test-secret-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        var filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, ex -> Mono.empty());

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isNull();
    }

    @Test
    @DisplayName("Filter - Malformed Authorization Header Should Reject")
    void filter_WithMalformedAuthHeader_ShouldReject() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/users")
                .header(HttpHeaders.AUTHORIZATION, "InvalidFormat token")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        var filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, ex -> Mono.empty());

        StepVerifier.create(result)
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
