package TODOListApp.auth_service.Controller;

import TODOListApp.auth_service.Client.UserServiceClient;
import TODOListApp.auth_service.Entity.User;
import TODOListApp.auth_service.Entity.UserPrincipal;
import TODOListApp.auth_service.Service.JwtService;
import TODOListApp.auth_service.Service.UserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Received Request to create user in AuthController");
        ResponseEntity<User> res = userServiceClient.createUser(user);
        System.out.println(res);
        return res;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        logger.info("Received Request to login");
        return ResponseEntity.ok(userDetailServiceImpl.verify(user));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        logger.info("Received Request to validate");
        try {
            String username = jwtService.extractUserName(token);
            Long userId = jwtService.extractUserId(token);
            boolean isExpired = jwtService.validateToken(token, new UserPrincipal(
                    new User(userId, username, "", "")));

            if (!isExpired) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired token");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("userId", userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/validate/header")
    public ResponseEntity<?> validateTokenbyHeader(@RequestHeader("Authorization") String authHeader) {
        logger.info("Received Request to validate header");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logger.info("Auth header received: {}", authHeader);
        String token = authHeader.substring(7);
        logger.info("Token after strip: {}", token);
        try {
            jwtService.validateAndExtract(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
