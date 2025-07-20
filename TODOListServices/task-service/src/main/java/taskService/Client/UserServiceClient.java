package taskService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service", path = "/")
public interface UserServiceClient {
    @GetMapping("user/id/{userID}")
    ResponseEntity<Boolean> isValidUser(@PathVariable("userID") Long userID);
}
