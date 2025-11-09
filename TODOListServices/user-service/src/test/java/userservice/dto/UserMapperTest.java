package userservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import userservice.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    @Test
    @DisplayName("NoArgsConstructor - Creates Empty Object")
    void noArgsConstructor_CreatesEmptyObject() {
        UserMapper mapper = new UserMapper();
    }

    @Test
    @DisplayName("convertToDTO - Success")
    void convertToDTO_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        UserDTO dto = UserMapper.convertToDTO(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("testuser");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("convertToDTO - Null User")
    void convertToDTO_NullUser() {
        UserDTO dto = UserMapper.convertToDTO(null);

        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("convertToEntity - Success")
    void convertToEntity_Success() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");

        User user = UserMapper.convertToEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("convertToEntity - Null DTO")
    void convertToEntity_NullDTO() {
        User user = UserMapper.convertToEntity(null);

        assertThat(user).isNull();
    }

    @Test
    @DisplayName("convertToDTO - Partial Data")
    void convertToDTO_PartialData() {
        User user = new User();
        user.setUsername("onlyusername");

        UserDTO dto = UserMapper.convertToDTO(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getUsername()).isEqualTo("onlyusername");
        assertThat(dto.getEmail()).isNull();
    }
}
