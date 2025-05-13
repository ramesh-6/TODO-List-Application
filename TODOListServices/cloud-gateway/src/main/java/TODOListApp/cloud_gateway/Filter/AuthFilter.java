//package TODOListApp.gateway_service.Filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class AuthFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Value("${auth-service.url}")
//    private String authServiceUrl;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Missing or invalid Authorization header");
//            return;
//        }
//
//        try {
//            ResponseEntity<String> authResponse = restTemplate.exchange(
//                    authServiceUrl + "/auth/validate/header",
//                    HttpMethod.POST,
//                    new HttpEntity<>(new HttpHeaders() {{
//                        set("Authorization", authHeader);
//                    }}),
//                    String.class
//            );
//
//            if (authResponse.getStatusCode().is2xxSuccessful()) {
//                filterChain.doFilter(request, response);
//            } else {
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                response.getWriter().write("Unauthorized - Invalid Token");
//            }
//
//        } catch (Exception e) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Unauthorized - Token Validation Failed");
//            logger.error("Error during token validation", e);
//        }
//    }
//}
