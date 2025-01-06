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

    @GetMapping("/Users")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/User/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/User")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @PostMapping("/Users")
    @ResponseStatus(HttpStatus.CREATED)
    public List<User> createUsers(@RequestBody List<User> users) {
        return this.userService.createUsers(users);
    }

    @PutMapping("/User/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        user.setId(id);
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/User/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") long id) {
        this.userService.deleteUser(id);
    }

}

