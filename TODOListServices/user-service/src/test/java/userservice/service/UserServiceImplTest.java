package userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import userservice.dto.UserDTO;
import userservice.entity.User;
import userservice.exception.DatabaseException;
import userservice.exception.NoUsersFoundException;
import userservice.exception.UserAlreadyExistException;
import userservice.exception.UserNotFoundException;
import userservice.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setUsername("testuser");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setPassword("password123");
    }

    @Test
    @DisplayName("createUser - Success")
    void createUser_Success() {
        when(userRepository.findByUsername(testUserDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.createUser(testUserDTO);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository).findByUsername(testUserDTO.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("createUser - User Already Exists")
    void createUser_UserAlreadyExists() {
        when(userRepository.findByUsername(testUserDTO.getUsername())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessageContaining("User already exists with username: testuser");

        verify(userRepository).findByUsername(testUserDTO.getUsername());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUser - Database Exception")
    void createUser_DatabaseException() {
        when(userRepository.findByUsername(testUserDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to create user");

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("isValidUser - User Exists")
    void isValidUser_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Boolean result = userService.isValidUser(1L);

        assertThat(result).isTrue();
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("isValidUser - User Not Found")
    void isValidUser_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.isValidUser(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with id: 999");

        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("isValidUser - Database Exception")
    void isValidUser_DatabaseException() {
        when(userRepository.findById(1L)).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> userService.isValidUser(1L))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to validate user");

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("updateUser - Success")
    void updateUser_Success() {
        when(userRepository.findById(testUserDTO.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(1L);
        updatedDTO.setUsername("updateduser");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setPassword("newpassword");

        UserDTO result = userService.updateUser(updatedDTO);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("updateduser");
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("updateUser - User Not Found")
    void updateUser_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserDTO updateDTO = new UserDTO();
        updateDTO.setId(999L);

        assertThatThrownBy(() -> userService.updateUser(updateDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User already exists with id: 999");

        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateUser - Database Exception")
    void updateUser_DatabaseException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> userService.updateUser(testUserDTO))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to update user");

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("getAllUser - Success")
    void getAllUser_Success() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        List<UserDTO> result = userService.getAllUser();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
        assertThat(result.get(1).getUsername()).isEqualTo("user2");
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getAllUser - No Users Found")
    void getAllUser_NoUsersFound() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> userService.getAllUser())
                .isInstanceOf(NoUsersFoundException.class)
                .hasMessageContaining("No users found in the system");

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getAllUser - Database Exception")
    void getAllUser_DatabaseException() {
        when(userRepository.findAll()).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> userService.getAllUser())
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to retrieve users");

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getUserById - Success")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("getUserById - User Not Found")
    void getUserById_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User already exists with id: 999");

        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("getUserById - Database Exception")
    void getUserById_DatabaseException() {
        when(userRepository.findById(1L)).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to retrieve user by ID");

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("findByUsername - Success")
    void findByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDTO result = userService.findByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    @DisplayName("findByUsername - User Not Found")
    void findByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("nonexistent"))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("findByUsername - Database Exception")
    void findByUsername_DatabaseException() {
        when(userRepository.findByUsername("testuser")).thenThrow(new DataAccessException("DB Error") {});

        assertThatThrownBy(() -> userService.findByUsername("testuser"))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to retrieve user by username");

        verify(userRepository).findByUsername("testuser");
    }

    @Test
    @DisplayName("deleteUser - Success")
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    @DisplayName("deleteUser - User Not Found")
    void deleteUser_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User already exists with id: 999");

        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteUser - Database Exception")
    void deleteUser_DatabaseException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doThrow(new DataAccessException("DB Error") {}).when(userRepository).delete(testUser);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Failed to delete user");

        verify(userRepository).delete(testUser);
    }
}
