package TODOListApp.auth_service.Service;

import TODOListApp.auth_service.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailService extends UserDetailsService {

    UserDetails loadUserByUsername(String username);

    String verify(User user);

}
