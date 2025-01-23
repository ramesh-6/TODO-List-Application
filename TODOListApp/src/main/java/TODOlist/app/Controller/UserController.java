package TODOlist.app.Controller;

import TODOlist.app.Entity.User;
import TODOlist.app.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/user/id/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }


    @GetMapping("/user/username/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public List<User> createUsers(@RequestBody List<User> users) {
        return this.userService.createUsers(users);
    }

    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        user.setId(id);
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") long id) {
        this.userService.deleteUser(id);
    }

}

