package TODOlist.app.Service;

import TODOlist.app.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User createUser(User user);

    List<User> createUsers(List<User> users);

    User updateUser(User user);

    List<User> getAllUser();

    User getUserById(long id);

    void deleteUser(long id);

    UserDetails loadUserByUsername(String username);
}

