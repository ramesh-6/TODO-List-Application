package TODOlist.app.Controller;

import TODOlist.app.Entity.Task;
import TODOlist.app.Entity.User;
import TODOlist.app.Service.TODOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private TODOService TODOService;

    @GetMapping("/Users")
    public List<User> getAllUser() {
        return TODOService.getAllUser();
    }

    @GetMapping("/Tasks")
    public List<Task> getAllTasks() {
        return TODOService.getAllTask();
    }

    @GetMapping("/User/{id}")
    public User getUserById(@PathVariable long id) {
        return TODOService.getUserById(id);
    }

    @GetMapping("/Task/{id}")
    public Task getTaskById(@PathVariable long id) {
        return TODOService.getTaskById(id);
    }

    @PostMapping("/User")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return this.TODOService.createUser(user);
    }

    @PostMapping("/Task")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        return this.TODOService.createTask(task);
    }

    @PostMapping("/Users")
    @ResponseStatus(HttpStatus.CREATED)
    public List<User> createUsers(@RequestBody List<User> users) {
        return this.TODOService.createUsers(users);
    }

    @PostMapping("/Tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Task> createTask(@RequestBody List<Task> tasks) {
        return this.TODOService.createTasks(tasks);
    }

    @PutMapping("/User/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        user.setId(id);
        return this.TODOService.updateUser(user);
    }

    @PutMapping("/Task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@PathVariable long id, @RequestBody Task task) {
        task.setId(id);
        return this.TODOService.updateTask(task);
    }

    @DeleteMapping("/User/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        this.TODOService.deleteUser(id);
    }

    @DeleteMapping("/Task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable long id) {
        this.TODOService.deleteTask(id);
    }

}
