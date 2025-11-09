package userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import userservice.dto.UserDTO;
import userservice.exception.NoUsersFoundException;
import userservice.exception.UserAlreadyExistException;
import userservice.exception.UserNotFoundException;
import userservice.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setUsername("testuser");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setPassword("password123");
    }

    @Test
    @DisplayName("getAllUser - Success")
    void getAllUser_Success() throws Exception {
        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        List<UserDTO> users = Arrays.asList(testUserDTO, user2);
        when(userService.getAllUser()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userService).getAllUser();
    }

    @Test
    @DisplayName("getAllUser - Empty List")
    void getAllUser_EmptyList() throws Exception {
        when(userService.getAllUser()).thenThrow(new NoUsersFoundException("No users found"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());

        verify(userService).getAllUser();
    }

    @Test
    @DisplayName("getUserById - Success")
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUserDTO);

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("getUserById - Not Found")
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(999L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/user/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(999L);
    }

    @Test
    @DisplayName("isValidUser - User Exists")
    void isValidUser_UserExists() throws Exception {
        when(userService.isValidUser(1L)).thenReturn(true);

        mockMvc.perform(get("/user/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService).isValidUser(1L);
    }

    @Test
    @DisplayName("isValidUser - User Not Found")
    void isValidUser_UserNotFound() throws Exception {
        when(userService.isValidUser(999L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/user/id/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(userService).isValidUser(999L);
    }

    @Test
    @DisplayName("findByUsername - Success")
    void findByUsername_Success() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(testUserDTO);

        mockMvc.perform(get("/user/username/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).findByUsername("testuser");
    }

    @Test
    @DisplayName("findByUsername - Not Found")
    void findByUsername_NotFound() throws Exception {
        when(userService.findByUsername("nonexistent")).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/user/username/{username}", "nonexistent"))
                .andExpect(status().isNotFound());

        verify(userService).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("createUser - Success")
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(testUserDTO);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("createUser - User Already Exists")
    void createUser_UserAlreadyExists() throws Exception {
        when(userService.createUser(any(UserDTO.class)))
                .thenThrow(new UserAlreadyExistException("User already exists"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isConflict());

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("updateUser - Success")
    void updateUser_Success() throws Exception {
        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updated@example.com");

        when(userService.updateUser(any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService).updateUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("updateUser - User Not Found")
    void updateUser_UserNotFound() throws Exception {
        when(userService.updateUser(any(UserDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/user/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isNotFound());

        verify(userService).updateUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("deleteUser - Success")
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("deleteUser - User Not Found")
    void deleteUser_UserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found"))
                .when(userService).deleteUser(999L);

        mockMvc.perform(delete("/user/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(999L);
    }

    @Test
    @DisplayName("createUser - Invalid JSON")
    void createUser_InvalidJSON() throws Exception {
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("updateUser - Sets ID from Path Variable")
    void updateUser_SetsIdFromPathVariable() throws Exception {
        UserDTO updateDTO = new UserDTO();
        updateDTO.setUsername("updated");
        updateDTO.setEmail("updated@example.com");

        when(userService.updateUser(any(UserDTO.class))).thenReturn(testUserDTO);

        mockMvc.perform(put("/user/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        verify(userService).updateUser(argThat(user -> user.getId() == 5L));
    }
}
