package TODOListApp.auth_service.Controller;

import TODOListApp.auth_service.Client.UserServiceClient;
import TODOListApp.auth_service.Entity.User;
import TODOListApp.auth_service.Entity.UserPrincipal;
import TODOListApp.auth_service.Service.JwtService;
import TODOListApp.auth_service.Service.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserServiceClient userServiceClient;
    private final UserDetailService userDetailService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserServiceClient userServiceClient, UserDetailService userDetailService, JwtService jwtService) {
        this.userServiceClient = userServiceClient;
        this.userDetailService = userDetailService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Received Request to create user");
        ResponseEntity<User> res = userServiceClient.createUser(user);
        logger.info("Created user: {}", res.getBody().getUsername());
        return res;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        logger.info("Received Request to login");
        try {
            String token = userDetailService.verify(user);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            logger.error("Login failed for user: {}", user.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestBody String token) {
        logger.info("Received Request to validate");
        try {
            String username = jwtService.extractUserName(token);
            Long userId = jwtService.extractUserId(token);
            boolean valid = jwtService.validateToken(token, new UserPrincipal(new User(userId, username, "", "")));

            if (!valid) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token expired");
            }

            Map<String, Object> response = Map.of(
                    "username", username,
                    "userId", userId
            );
            logger.info("JWT Token validated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Token validation failed", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid jwt token");
        }
    }

    @PostMapping("/validate/header")
    public ResponseEntity<Void> validateByHeader(@RequestHeader("Authorization") String authHeader) {
        logger.info("Received Request to validate via header");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }

        String token = authHeader.substring(7);
        logger.debug("Token after removing Bearer prefix: {}", token);

        try {
            jwtService.validateAndExtract(token);
            logger.info("JWT token in header validated");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid jwt token");
        }
    }
}
