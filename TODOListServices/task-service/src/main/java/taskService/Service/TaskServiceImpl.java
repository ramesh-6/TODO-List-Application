package taskService.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskService.Client.UserServiceClient;
import taskService.DTO.TaskDTO;
import taskService.DTO.TaskMapper;
import taskService.Entity.Task;
import taskService.Exception.TaskNotFoundException;
import taskService.Exception.UserNotFoundException;
import taskService.Repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserServiceClient userServiceClient) {
        this.taskRepository = taskRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        logger.info("Creating task: {}",taskDTO);
        return TaskMapper.convertToDTO(taskRepository.save(TaskMapper.convertToEntity(taskDTO)));
    }

    @Override
    public List<TaskDTO> createTasks(List<TaskDTO> taskDTOS) {
        logger.info("Creating tasks: {}",taskDTOS);
        List<Task> taskList = taskDTOS.stream().map(TaskMapper::convertToEntity).toList();
        List<Task> createdTasks = taskRepository.saveAll(taskList);
        return createdTasks.stream().map(TaskMapper::convertToDTO).toList();
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDTO) {
        logger.info("Updating task: {}",taskDTO);
        Optional<Task> taskDB = this.taskRepository.findById(taskDTO.getId());
        if (taskDB.isPresent()) {
            Task taskUpdate = taskDB.get();
            taskUpdate.setId(taskDTO.getId());
            taskUpdate.setTitle(taskDTO.getTitle());
            taskUpdate.setDescription(taskDTO.getDescription());
            taskUpdate.setUserID(taskDTO.getUserID());
            taskUpdate.setCompleted(taskDTO.getCompleted());
            taskRepository.save(taskUpdate);
            logger.info("Updated task: {}",taskUpdate);
            return TaskMapper.convertToDTO(taskUpdate);
        } else {
            throw new TaskNotFoundException(taskDTO.getId());
        }
    }

    @Override
    public List<TaskDTO> getAllTask() {
        logger.info("Getting all tasks");
        return taskRepository.findAll().stream().map(TaskMapper::convertToDTO).toList();
    }

    @Override
    public TaskDTO getTaskById(long taskId) {
        logger.info("Getting task with ID: {}",taskId);
        Optional<Task> taskDB = this.taskRepository.findById(taskId);
        if (taskDB.isPresent()) {
            return TaskMapper.convertToDTO(taskDB.get());
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

    @Override
    public List<TaskDTO> getTasksByUserId(long userId) {
        logger.info("Getting all tasks with userID: {}",userId);
        ResponseEntity<Boolean> res = userServiceClient.isValidUser(userId);
        System.out.println(res);
        Boolean isValidUser = res.getBody();
        System.out.println(isValidUser);
        if(isValidUser == null || !isValidUser){
            throw new UserNotFoundException(userId);
        }
        else{
            return taskRepository.findByUserID(userId).stream().map(TaskMapper::convertToDTO).toList();
        }
    }

    @Override
    public void deleteTask(long taskId) {
        logger.info("Deleting task by ID: {}",taskId);
        Optional<Task> taskDB = this.taskRepository.findById(taskId);

        if (taskDB.isPresent()) {
            this.taskRepository.delete(taskDB.get());
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }
}
