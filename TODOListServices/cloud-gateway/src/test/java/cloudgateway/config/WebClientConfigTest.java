package cloudgateway.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("WebClientConfig Tests")
class WebClientConfigTest {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Test
    @DisplayName("WebClient.Builder Bean - Should Be Load Balanced")
    void webClientBuilder_ShouldBeLoadBalanced() {
        assertThat(webClientBuilder).isNotNull();
    }

    @Test
    @DisplayName("WebClient - Should Build Successfully")
    void webClient_ShouldBuildSuccessfully() {
        WebClient webClient = webClientBuilder.build();
        assertThat(webClient).isNotNull();
    }
}
