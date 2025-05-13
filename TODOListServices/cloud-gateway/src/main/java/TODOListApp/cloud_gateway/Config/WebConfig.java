//package TODOListApp.gateway_service.Config;
//
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//
////    @Bean
////    public CorsFilter corsFilter() {
////        CorsConfiguration config = new CorsConfiguration();
////        config.addAllowedOrigin("http://localhost:3000");
////        config.addAllowedMethod("GET");
////        config.addAllowedMethod("POST");
////        config.addAllowedMethod("PUT");
////        config.addAllowedMethod("DELETE");
////        config.addAllowedMethod("OPTIONS");
////        config.addAllowedHeader("*");
////        config.setAllowCredentials(true);
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter(source);
////    }
//}
