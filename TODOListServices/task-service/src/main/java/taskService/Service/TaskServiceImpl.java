package taskService.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskService.Client.UserServiceClient;
import taskService.DTO.TaskDTO;
import taskService.DTO.TaskMapper;
import taskService.Entity.Task;
import taskService.Exception.*;
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
        logger.info("Creating task: {}", taskDTO);
        try {
            ResponseEntity<Boolean> res = userServiceClient.isValidUser(taskDTO.getUserID());
            Boolean isValidUser = res.getBody();

            if (isValidUser == null || !isValidUser) {
                throw new UserNotFoundException("User not found");
            }

            List<Task> userTasks = taskRepository.findByUserID(taskDTO.getUserID());

            boolean taskExists = userTasks.stream()
                    .anyMatch(existingTask ->
                            existingTask.getTitle().equals(taskDTO.getTitle()) &&
                                    existingTask.getDescription().equals(taskDTO.getDescription())
                    );

            if (taskExists) {
                throw new TaskAlreadyExistsException("Task with title '" + taskDTO.getTitle() +
                        "' and description '" + taskDTO.getDescription() + "' already exists for user: " + taskDTO.getUserID());
            }

            Task savedTask = taskRepository.save(TaskMapper.convertToEntity(taskDTO));
            logger.info("Successfully created task with ID: {}", savedTask.getId());
            return TaskMapper.convertToDTO(savedTask);

        } catch (DataAccessException e) {
            logger.error("Database error while creating task: {}", taskDTO.getTitle(), e);
            throw new DatabaseException("Failed to create task", e);
        }
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDTO) {
        logger.info("Updating task: {}", taskDTO);
        try {
            Optional<Task> taskDB = taskRepository.findById(taskDTO.getId());
            if (taskDB.isPresent()) {
                Task taskUpdate = taskDB.get();
                taskUpdate.setId(taskDTO.getId());
                taskUpdate.setTitle(taskDTO.getTitle());
                taskUpdate.setDescription(taskDTO.getDescription());
                taskUpdate.setUserID(taskDTO.getUserID());
                taskUpdate.setCompleted(taskDTO.getCompleted());
                taskRepository.save(taskUpdate);
                logger.info("Updated task: {}", taskUpdate);
                return TaskMapper.convertToDTO(taskUpdate);
            } else {
                throw new TaskNotFoundException(taskDTO.getId());
            }
        } catch (DataAccessException e) {
            logger.error("Database error while updating task: {}", taskDTO.getId(), e);
            throw new DatabaseException("Failed to update task", e);
        }
    }

    @Override
    public List<TaskDTO> getAllTask() {
        logger.info("Getting all tasks");
        try {
            List<Task> tasks = taskRepository.findAll();
            if (tasks.isEmpty()) {
                throw new NoTasksFoundException("No tasks found in the system");
            }
            return tasks.stream().map(TaskMapper::convertToDTO).toList();
        } catch (DataAccessException e) {
            logger.error("Database error while fetching all tasks", e);
            throw new DatabaseException("Failed to retrieve tasks", e);
        }
    }

    @Override
    public TaskDTO getTaskById(long taskId) {
        logger.info("Getting task with ID: {}", taskId);
        try {
            Optional<Task> taskDB = taskRepository.findById(taskId);
            if (taskDB.isPresent()) {
                return TaskMapper.convertToDTO(taskDB.get());
            } else {
                throw new TaskNotFoundException(taskId);
            }
        } catch (DataAccessException e) {
            logger.error("Database error while fetching task by ID: {}", taskId, e);
            throw new DatabaseException("Failed to retrieve task by ID", e);
        }
    }

    @Override
    public List<TaskDTO> getTasksByUserId(long userId) {
        logger.info("Getting all tasks with userID: {}", userId);
        try {
            ResponseEntity<Boolean> res = userServiceClient.isValidUser(userId);
            Boolean isValidUser = res.getBody();

            if (isValidUser == null || !isValidUser) {
                throw new UserNotFoundException("User not found");
            }

            List<Task> userTasks = taskRepository.findByUserID(userId);
            return userTasks.stream().map(TaskMapper::convertToDTO).toList();

        } catch (DataAccessException e) {
            logger.error("Database error while fetching tasks for user: {}", userId, e);
            throw new DatabaseException("Failed to retrieve tasks for user", e);
        }
    }

    @Override
    public void deleteTask(long taskId) {
        logger.info("Deleting task by ID: {}", taskId);
        try {
            Optional<Task> taskDB = taskRepository.findById(taskId);
            if (taskDB.isPresent()) {
                taskRepository.delete(taskDB.get());
            } else {
                throw new TaskNotFoundException(taskId);
            }
        } catch (DataAccessException e) {
            logger.error("Database error while deleting task: {}", taskId, e);
            throw new DatabaseException("Failed to delete task", e);
        }
    }
}
