package TODOlist.app.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("TODO List Application").version("1.0").description("API documentation for TODO List application"));
    }
}
