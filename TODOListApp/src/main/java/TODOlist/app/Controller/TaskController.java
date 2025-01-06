package TODOlist.app.Controller;

import TODOlist.app.Entity.Task;
import TODOlist.app.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService TaskService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return TaskService.getAllTask();
    }

    @GetMapping("/task/{id}")
    public Task getTaskById(@PathVariable("id") long id) {
        return TaskService.getTaskById(id);
    }

    @GetMapping("/tasks/{id}")
    public List<Task> getTasksById(@PathVariable("id") long id) {
        return TaskService.getTasksByUserId(id);
    }

    @PostMapping("/task")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        return this.TaskService.createTask(task);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Task> createTask(@RequestBody List<Task> tasks) {
        return this.TaskService.createTasks(tasks);
    }

    @PutMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@PathVariable("id") long id, @RequestBody Task task) {
        task.setId(id);
        return this.TaskService.updateTask(task);
    }

    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable("id") long id) {
        this.TaskService.deleteTask(id);
    }

}
