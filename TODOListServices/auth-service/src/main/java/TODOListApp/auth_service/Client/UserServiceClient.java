package TODOListApp.auth_service.Client;

import TODOListApp.auth_service.Config.FeignClientConfig;
import TODOListApp.auth_service.Entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="cloud-gateway", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @PostMapping("/user")
    ResponseEntity<User> createUser(@RequestBody User user);

    @GetMapping("/user/username/{username}")
    ResponseEntity<User> findByUsername(@PathVariable("username")String username);
}
