package TODOListApp.auth_service.Client;

import TODOListApp.auth_service.Entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-service", path = "/user-service")
public interface UserServiceClient {

//    @GetMapping("Users/{userID}")
//    ResponseEntity<Boolean> isValidUser(@PathVariable("userID")Long userID);

    @PostMapping("/User")
    ResponseEntity<User> createUser(@RequestBody User user);

    @GetMapping("/User/username/{username}")
    ResponseEntity<User> findByUsername(@PathVariable("username")String username);
}
