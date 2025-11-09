package taskservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import taskservice.client.UserServiceClient;
import taskservice.dto.TaskDTO;
import taskservice.entity.Task;
import taskservice.exception.*;
import taskservice.repository.TaskRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskServiceImpl Tests")
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;
    private TaskDTO testTaskDTO;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setUserID(100L);
        testTask.setCompleted(false);

        testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setTitle("Test Task");
        testTaskDTO.setDescription("Test Description");
        testTaskDTO.setUserID(100L);
        testTaskDTO.setCompleted(false);
    }

    @Test
    @DisplayName("createTask - Success")
    void createTask_Success() {
        when(userServiceClient.isValidUser(100L)).thenReturn(ResponseEntity.ok(true));
        when(taskRepository.findByUserID(100L)).thenReturn(Collections.emptyList());
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDTO result = taskService.createTask(testTaskDTO);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(userServiceClient).isValidUser(100L);
        verify(taskRepository).findByUserID(100L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("createTask - User Not Found")
    void createTask_UserNotFound() {
        when(userServiceClient.isValidUser(999L)).thenReturn(ResponseEntity.ok(false));

        testTaskDTO.setUserID(999L);

        assertThatThrownBy(() -> taskService.createTask(testTaskDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userServiceClient).isValidUser(999L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask - User Service Returns Null")
    void createTask_UserServiceReturnsNull() {
        when(userServiceClient.isValidUser(100L)).thenReturn(ResponseEntity.ok(null));

        assertThatThrownBy(() -> taskService.createTask(testTaskDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userServiceClient).isValidUser(100L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask - Task Already Exists")
    void createTask_TaskAlreadyExists() {
        Task existingTask = new Task();
        existingTask.setTitle("Test Task");
        existingTask.setDescription("Test Description");

        when(userServiceClient.isValidUser(100L)).thenReturn(ResponseEntity.ok(true));
        when(taskRepository.findByUserID(100L)).thenReturn(Arrays.asList(existingTask));

        assertThatThrownBy(() -> taskService.createTask(testTaskDTO))
                .isInstanceOf(TaskAlreadyExistsException.class)
                .hasMessageContaining("Task with title 'Test Task' and description 'Test Description' already exists for user: 100");

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask - Database Exception")
    void createTask_DatabaseException() {
        when(userServiceClient.isValidUser(100L)).thenReturn(ResponseEntity.ok(true));
        when(taskRepository.findByUserID(100L)).thenReturn(Collections.emptyList());
        when(taskRepository.save(any(Task.class))).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> taskService.createTask(testTaskDTO))
                .isInstanceOf(DatabaseException.class)
                .hasMessage("Failed to create task");

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - Success")
    void updateTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDTO result = taskService.updateTask(testTaskDTO);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - Task Not Found")
    void updateTask_TaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        testTaskDTO.setId(999L);

        assertThatThrownBy(() -> taskService.updateTask(testTaskDTO))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask - Database Exception")
    void updateTask_DatabaseException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> taskService.updateTask(testTaskDTO))
                .isInstanceOf(DatabaseException.class)
                .hasMessage("Failed to update task");

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("getAllTask - Success")
    void getAllTask_Success() {
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(testTask, task2));

        List<TaskDTO> result = taskService.getAllTask();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("getAllTask - No Tasks Found")
    void getAllTask_NoTasksFound() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> taskService.getAllTask())
                .isInstanceOf(NoTasksFoundException.class)
                .hasMessage("No tasks found in the system");

        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("getAllTask - Database Exception")
    void getAllTask_DatabaseException() {
        when(taskRepository.findAll()).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> taskService.getAllTask())
                .isInstanceOf(DatabaseException.class)
                .hasMessage("Failed to retrieve tasks");

        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("getTaskById - Success")
    void getTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        TaskDTO result = taskService.getTaskById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("getTaskById - Task Not Found")
    void getTaskById_TaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(999L);
    }

    @Test
    @DisplayName("getTaskById - Database Exception")
    void getTaskById_DatabaseException() {
        when(taskRepository.findById(1L)).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> taskService.getTaskById(1L))
                .isInstanceOf(DatabaseException.class)
                .hasMessage("Failed to retrieve task by ID");

        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("getTasksByUserId - Success")
    void getTasksByUserId_Success() {
        when(userServiceClient.isValidUser(100L)).thenReturn(ResponseEntity.ok(true));
        when(taskRepository.findByUserID(100L)).thenReturn(Arrays.asList(testTask));

        List<TaskDTO> result = taskService.getTasksByUserId(100L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Task");
        verify(userServiceClient).isValidUser(100L);
        verify(taskRepository).findByUserID(100L);
    }

    @Test
    @DisplayName("getTasksByUserId - User Not Found")
    void getTasksByUserId_UserNotFound() {
        when(userServiceClient.isValidUser(999L)).thenReturn(ResponseEntity.ok(false));

        assertThatThrownBy(() -> taskService.getTasksByUserId(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userServiceClient).isValidUser(999L);
        verify(taskRepository, never()).findByUserID(any());
    }

    @Test
    @DisplayName("getTasksByUserId - Database Exception")
    void getTasksByUserId_DatabaseException() {
        when(userServiceClient.isValidUser(100L)).thenReturn(ResponseEntity.ok(true));
        when(taskRepository.findByUserID(100L)).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> taskService.getTasksByUserId(100L))
                .isInstanceOf(DatabaseException.class)
                .hasMessage("Failed to retrieve tasks for user");

        verify(taskRepository).findByUserID(100L);
    }

    @Test
    @DisplayName("deleteTask - Success")
    void deleteTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        doNothing().when(taskRepository).delete(testTask);

        taskService.deleteTask(1L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(testTask);
    }

    @Test
    @DisplayName("deleteTask - Task Not Found")
    void deleteTask_TaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(999L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteTask - Database Exception")
    void deleteTask_DatabaseException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        doThrow(new DataAccessException("DB Error") {}).when(taskRepository).delete(testTask);

        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(DatabaseException.class)
                .hasMessage("Failed to delete task");

        verify(taskRepository).delete(testTask);
    }
}
