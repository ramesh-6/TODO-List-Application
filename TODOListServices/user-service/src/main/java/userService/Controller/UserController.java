package userService.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userService.DTO.UserDTO;
import userService.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user-service")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/Users")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        logger.info("Received Request to get all users");
        List<UserDTO> userDTOS = userService.getAllUser();
        return ResponseEntity.ok(userDTOS);
    }

    @GetMapping("/User/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        logger.info("Received Request to get user by ID: {}",id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/Users/id/{id}")
    public ResponseEntity<Boolean> isValidUser(@PathVariable long id) {
        logger.info("Received Request to validate user by ID: {}",id);
        return ResponseEntity.ok(userService.isValidUser(id));
    }

    @GetMapping("/User/username/{username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username){
        logger.info("Received Request to find user by username: {}",username);
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PostMapping("/User")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        logger.info("Received Request to create user");
        return new ResponseEntity<>(userService.createUser(userDTO),HttpStatus.CREATED);
    }

    @PostMapping("/Users")
    public ResponseEntity<List<UserDTO>> createUsers(@RequestBody List<UserDTO> userDTOS) {
        logger.info("Received Request to create users");
        return new ResponseEntity<>(userService.createUsers(userDTOS),HttpStatus.CREATED);
    }

    @PutMapping("/User/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        logger.info("Received Request to update user by ID: {}",id);
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @DeleteMapping("/User/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        logger.info("Received Request to delete user by ID: {}",id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
