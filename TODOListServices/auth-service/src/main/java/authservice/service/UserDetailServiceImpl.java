package authservice.service;

import authservice.client.UserServiceClient;
import authservice.entity.User;
import authservice.entity.UserPrincipal;
import authservice.exception.AuthenticationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    private final UserServiceClient userServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    public UserDetailServiceImpl(@Lazy AuthenticationManager authManager, JwtService jwtService, UserServiceClient userServiceClient) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}",username);
        try {
            ResponseEntity<User> res = userServiceClient.findByUsername(username);
            User user = res.getBody();
            if (user == null) {
                System.out.println("User Not Found");
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return new UserPrincipal(user);
        } catch (Exception e) {
            logger.error("Exception when trying to fetch user '{}' via Feign client: {}", username, e.toString(), e);
            throw new UsernameNotFoundException("Failed to load user: " + username, e);
        }
    }

    public String verify(User user) {
        logger.info("Verifying user");
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                ResponseEntity<User> res = userServiceClient.findByUsername(user.getUsername());
                User authenticatedUser = res.getBody();
                logger.info("Authenticated username: {}, id: {}", authenticatedUser.getUsername(), authenticatedUser.getId());
                return jwtService.generateToken(authenticatedUser.getUsername(), authenticatedUser.getId());
            } else {
                logger.warn("Authentication failed for user: {}", user.getUsername());
                throw new AuthenticationFailedException("Authentication failed for user: " + user.getUsername());
            }
        } catch (Exception e) {
            logger.error("Exception during verification of user '{}': {}", user.getUsername(), e.toString(), e);
            throw new AuthenticationFailedException("Authentication failed for user: " + user.getUsername());
        }
    }


}
