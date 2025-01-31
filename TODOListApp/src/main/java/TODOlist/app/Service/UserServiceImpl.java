package TODOlist.app.Service;

import TODOlist.app.Entity.User;
import TODOlist.app.Entity.UserPrincipal;
import TODOlist.app.Exception.UserNotFoundException;
import TODOlist.app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }


    @Override
    public User getUserById(long UserId) {
        Optional<User> userDB = this.userRepository.findById(UserId);
        if (userDB.isPresent()) {
            return userDB.get();
        } else {
            throw new UserNotFoundException("Record not found with id : " + UserId);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        return user;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> createUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> userDB = this.userRepository.findById(user.getId());
        if (userDB.isPresent()) {
            User userUpdate = userDB.get();
            userUpdate.setId(user.getId());
            userUpdate.setUsername(user.getUsername());
            userUpdate.setPassword(user.getPassword());
            userUpdate.setEmail(user.getEmail());
            userRepository.save(userUpdate);
            return userUpdate;
        } else {
            throw new UserNotFoundException("Record not found with id : " + user.getId());
        }
    }

    @Override
    public void deleteUser(long userId) {
        Optional<User> userDB = this.userRepository.findById(userId);
        if (userDB.isPresent()) {
            this.userRepository.delete(userDB.get());
        } else {
            throw new UserNotFoundException("Record not found with id : " + userId);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user);
    }

    @Override
    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            User authenticatedUser = userRepository.findByUsername(user.getUsername());
            System.out.println("Authenticated username: " + authenticatedUser.getUsername() + " id: " + authenticatedUser.getId());
            return jwtService.generateToken(authenticatedUser.getUsername(), authenticatedUser.getId());
        } else {
            return "fail";
        }
    }
}

