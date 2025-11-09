package cloudgateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final WebClient.Builder webClientBuilder;

    @Value("${gateway.secret}")
    private String internalTokenSecret;

    public JwtAuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String requestUri = exchange.getRequest().getURI().getPath();
            logger.debug("Incoming request: {}", requestUri);

            String gatewayToken = exchange.getRequest().getHeaders().getFirst("X-Gateway-Token");
            logger.debug("X-Gateway-Token received: {}", gatewayToken);
            if (gatewayToken != null && gatewayToken.equals(internalTokenSecret)) {
                logger.info("Bypassing JWT check for internal trusted request");
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClientBuilder.build()
                    .post()
                    .uri("lb://auth-service/auth/validate/header")
                    .header("Authorization", authHeader)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(response -> {
                        logger.info("JWT Token validated successfully");
                        return chain.filter(exchange);
                    })
                    .onErrorResume(ex -> {
                        logger.error("Exception during token validation: {}", ex.getMessage(), ex);
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    public static class Config {}
}
