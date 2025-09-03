package taskService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import taskService.Config.FeignClientConfig;

@FeignClient(name="cloud-gateway", configuration = FeignClientConfig.class)
public interface UserServiceClient {
    @GetMapping("user/id/{userID}")
    ResponseEntity<Boolean> isValidUser(@PathVariable("userID") Long userID);
}
