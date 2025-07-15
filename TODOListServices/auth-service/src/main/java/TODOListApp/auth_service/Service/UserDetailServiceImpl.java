package TODOListApp.auth_service.Service;

import TODOListApp.auth_service.Client.UserServiceClient;
import TODOListApp.auth_service.Entity.User;
import TODOListApp.auth_service.Entity.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Lazy
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserServiceClient userServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

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
            throw new UsernameNotFoundException("Failed to load user: " + username, e);
        }
    }

    public String verify(User user) {
        logger.info("Verifying user");
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            ResponseEntity<User> res = userServiceClient.findByUsername(user.getUsername());
            User authenticatedUser = res.getBody();
            System.out.println("Authenticated username: " + authenticatedUser.getUsername() + " id: " + authenticatedUser.getId());
            return jwtService.generateToken(authenticatedUser.getUsername(), authenticatedUser.getId());
        } else {
            return "fail";
        }
    }

}
