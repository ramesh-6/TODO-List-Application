package authservice.service;

import authservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailService extends UserDetailsService {

    UserDetails loadUserByUsername(String username);

    String verify(User user);

}
