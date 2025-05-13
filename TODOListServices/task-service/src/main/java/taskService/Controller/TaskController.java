package taskService.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskService.DTO.TaskDTO;
import taskService.Service.Service;

import java.util.List;

@RestController
@RequestMapping("/task-service")
public class TaskController {

    @Autowired
    private Service service;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/Tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        logger.info("Received Request to get all tasks");
        return ResponseEntity.ok(service.getAllTask());
    }


    @GetMapping("/Task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable long id) {
        logger.info("Received Request to get task by ID: {}",id);
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @GetMapping("/Tasks/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@PathVariable long id) {
        logger.info("Received Request to get task by userID: {}",id);
        return ResponseEntity.ok(service.getTasksByUserId(id));
    }

    @PostMapping("/Task")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        logger.info("Received Request to create task");
        return new ResponseEntity<>(service.createTask(taskDTO),HttpStatus.CREATED);
    }

    @PostMapping("/Tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<TaskDTO>> createTasks(@RequestBody List<TaskDTO> taskDTOS) {
        logger.info("Received Request to create tasks");
        return new ResponseEntity<>(service.createTasks(taskDTOS),HttpStatus.CREATED);
    }

    @PutMapping("/Task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskDTO> updateTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
        logger.info("Received Request to update task by ID and data: {}, {}",id,taskDTO);
        taskDTO.setId(id);
        return ResponseEntity.ok(service.updateTask(taskDTO));
    }

    @DeleteMapping("/Task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        logger.info("Received Request to delete task by ID: {}",id);
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
