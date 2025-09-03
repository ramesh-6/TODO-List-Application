package taskService.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskService.DTO.TaskDTO;
import taskService.Service.TaskService;

import java.net.URI;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks(HttpServletRequest request) {
        logger.info("Received Request to get all tasks");
        return ResponseEntity.ok(taskService.getAllTask());
    }


    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable long id) {
        logger.info("Received Request to get task by ID: {}",id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/tasks/id/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@PathVariable long id) {
        logger.info("Received Request to get task by userID: {}",id);
        return ResponseEntity.ok(taskService.getTasksByUserId(id));
    }

    @PostMapping("/task")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        logger.info("Received Request to create task");
        return new ResponseEntity<>(taskService.createTask(taskDTO),HttpStatus.CREATED);
    }

    @PutMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskDTO> updateTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
        logger.info("Received Request to update task by ID and data: {}, {}",id,taskDTO);
        taskDTO.setId(id);
        return ResponseEntity.ok(taskService.updateTask(taskDTO));
    }

    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        logger.info("Received Request to delete task by ID: {}",id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
