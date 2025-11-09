package taskservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import taskservice.dto.TaskDTO;
import taskservice.exception.NoTasksFoundException;
import taskservice.exception.TaskAlreadyExistsException;
import taskservice.exception.TaskNotFoundException;
import taskservice.exception.UserNotFoundException;
import taskservice.service.TaskService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@DisplayName("TaskController Tests")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDTO testTaskDTO;

    @BeforeEach
    void setUp() {
        testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setTitle("Test Task");
        testTaskDTO.setDescription("Test Description");
        testTaskDTO.setUserID(100L);
        testTaskDTO.setCompleted(false);
    }

    @Test
    @DisplayName("getAllTasks - Success")
    void getAllTasks_Success() throws Exception {
        TaskDTO task2 = new TaskDTO();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setUserID(100L);
        task2.setCompleted(true);

        List<TaskDTO> tasks = Arrays.asList(testTaskDTO, task2);
        when(taskService.getAllTask()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].completed").value(true));

        verify(taskService).getAllTask();
    }

    @Test
    @DisplayName("getAllTasks - No Tasks Found")
    void getAllTasks_NoTasksFound() throws Exception {
        when(taskService.getAllTask()).thenThrow(new NoTasksFoundException("No tasks found"));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isNotFound());

        verify(taskService).getAllTask();
    }

    @Test
    @DisplayName("getTaskById - Success")
    void getTaskById_Success() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(testTaskDTO);

        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.userID").value(100))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).getTaskById(1L);
    }

    @Test
    @DisplayName("getTaskById - Not Found")
    void getTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(999L)).thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(get("/task/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(taskService).getTaskById(999L);
    }

    @Test
    @DisplayName("getTasksByUserId - Success")
    void getTasksByUserId_Success() throws Exception {
        List<TaskDTO> userTasks = Arrays.asList(testTaskDTO);
        when(taskService.getTasksByUserId(100L)).thenReturn(userTasks);

        mockMvc.perform(get("/tasks/id/{id}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userID").value(100));

        verify(taskService).getTasksByUserId(100L);
    }

    @Test
    @DisplayName("getTasksByUserId - User Not Found")
    void getTasksByUserId_UserNotFound() throws Exception {
        when(taskService.getTasksByUserId(999L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/tasks/id/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(taskService).getTasksByUserId(999L);
    }

    @Test
    @DisplayName("createTask - Success")
    void createTask_Success() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(testTaskDTO);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.userID").value(100))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).createTask(any(TaskDTO.class));
    }

    @Test
    @DisplayName("createTask - Task Already Exists")
    void createTask_TaskAlreadyExists() throws Exception {
        when(taskService.createTask(any(TaskDTO.class)))
                .thenThrow(new TaskAlreadyExistsException("Task already exists"));

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskDTO)))
                .andExpect(status().isConflict());

        verify(taskService).createTask(any(TaskDTO.class));
    }

    @Test
    @DisplayName("createTask - User Not Found")
    void createTask_UserNotFound() throws Exception {
        when(taskService.createTask(any(TaskDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskDTO)))
                .andExpect(status().isNotFound());

        verify(taskService).createTask(any(TaskDTO.class));
    }

    @Test
    @DisplayName("updateTask - Success")
    void updateTask_Success() throws Exception {
        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setUserID(100L);
        updatedTask.setCompleted(true);

        when(taskService.updateTask(any(TaskDTO.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/task/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService).updateTask(any(TaskDTO.class));
    }

    @Test
    @DisplayName("updateTask - Task Not Found")
    void updateTask_TaskNotFound() throws Exception {
        when(taskService.updateTask(any(TaskDTO.class)))
                .thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(put("/task/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskDTO)))
                .andExpect(status().isNotFound());

        verify(taskService).updateTask(any(TaskDTO.class));
    }

    @Test
    @DisplayName("updateTask - Sets ID from Path Variable")
    void updateTask_SetsIdFromPathVariable() throws Exception {
        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("Updated");
        updateDTO.setDescription("Updated Description");

        when(taskService.updateTask(any(TaskDTO.class))).thenReturn(testTaskDTO);

        mockMvc.perform(put("/task/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        verify(taskService).updateTask(argThat(task -> task.getId() == 5L));
    }

    @Test
    @DisplayName("deleteTask - Success")
    void deleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/task/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }

    @Test
    @DisplayName("deleteTask - Task Not Found")
    void deleteTask_TaskNotFound() throws Exception {
        doThrow(new TaskNotFoundException(999L))
                .when(taskService).deleteTask(999L);

        mockMvc.perform(delete("/task/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(taskService).deleteTask(999L);
    }

    @Test
    @DisplayName("createTask - Invalid JSON")
    void createTask_InvalidJSON() throws Exception {
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }
}
