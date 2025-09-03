package taskService.Config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Value("${gateway.secret}")
    private String gatewaySecret;

    @Bean
    public RequestInterceptor gatewayTokenInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Gateway-Token", gatewaySecret);
        };
    }
}
