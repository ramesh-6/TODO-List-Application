package userservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDTO Tests")
class UserDTOTest {

    @Test
    @DisplayName("NoArgsConstructor - Creates Empty Object")
    void noArgsConstructor_CreatesEmptyObject() {
        UserDTO dto = new UserDTO();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getEmail()).isNull();
        assertThat(dto.getPassword()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor - Sets All Fields")
    void allArgsConstructor_SetsAllFields() {
        UserDTO dto = new UserDTO(1L, "testuser", "password123", "test@example.com");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("testuser");
        assertThat(dto.getPassword()).isEqualTo("password123");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Equals - Identical Objects")
    void equals_IdenticalObjects() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", "pass", "user@test.com");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto2).isEqualTo(dto1);
    }

    @Test
    @DisplayName("Equals - Same Object")
    void equals_SameObject() {
        UserDTO dto = new UserDTO(1L, "user", "pass", "user@test.com");

        assertThat(dto).isEqualTo(dto);
    }

    @Test
    @DisplayName("Equals - Null Object")
    void equals_NullObject() {
        UserDTO dto = new UserDTO(1L, "user", "pass", "user@test.com");

        assertThat(dto).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Equals - Different Class")
    void equals_DifferentClass() {
        UserDTO dto = new UserDTO(1L, "user", "pass", "user@test.com");
        String differentClass = "string";

        assertThat(dto).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Equals - Different Id")
    void equals_DifferentId() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(2L, "user", "pass", "user@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Different Username")
    void equals_DifferentUsername() {
        UserDTO dto1 = new UserDTO(1L, "user1", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user2", "pass", "user@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Different Password")
    void equals_DifferentPassword() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass1", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", "pass2", "user@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Different Email")
    void equals_DifferentEmail() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user1@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", "pass", "user2@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Null Id")
    void equals_NullId() {
        UserDTO dto1 = new UserDTO(null, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(null, "user", "pass", "user@test.com");

        assertThat(dto1).isEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - One Null Id")
    void equals_OneNullId() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(null, "user", "pass", "user@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Null Username")
    void equals_NullUsername() {
        UserDTO dto1 = new UserDTO(1L, null, "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, null, "pass", "user@test.com");

        assertThat(dto1).isEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - One Null Username")
    void equals_OneNullUsername() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, null, "pass", "user@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Null Password")
    void equals_NullPassword() {
        UserDTO dto1 = new UserDTO(1L, "user", null, "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", null, "user@test.com");

        assertThat(dto1).isEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - One Null Password")
    void equals_OneNullPassword() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", null, "user@test.com");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - Null Email")
    void equals_NullEmail() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", null);
        UserDTO dto2 = new UserDTO(1L, "user", "pass", null);

        assertThat(dto1).isEqualTo(dto2);
    }

    @Test
    @DisplayName("Equals - One Null Email")
    void equals_OneNullEmail() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", "pass", null);

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("HashCode - Same Objects Same HashCode")
    void hashCode_SameObjectsSameHashCode() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(1L, "user", "pass", "user@test.com");

        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("HashCode - Different Objects Different HashCode")
    void hashCode_DifferentObjectsDifferentHashCode() {
        UserDTO dto1 = new UserDTO(1L, "user1", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(2L, "user2", "pass", "user@test.com");

        assertThat(dto1.hashCode()).isNotEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("HashCode - Null Fields")
    void hashCode_NullFields() {
        UserDTO dto1 = new UserDTO(null, null, null, null);
        UserDTO dto2 = new UserDTO(null, null, null, null);

        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("HashCode - Same Object Multiple Calls")
    void hashCode_SameObjectMultipleCalls() {
        UserDTO dto = new UserDTO(1L, "user", "pass", "user@test.com");

        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("ToString - Contains All Fields")
    void toString_ContainsAllFields() {
        UserDTO dto = new UserDTO(1L, "testuser", "password", "test@example.com");

        String toString = dto.toString();

        assertThat(toString).contains("1");
        assertThat(toString).contains("testuser");
        assertThat(toString).contains("password");
        assertThat(toString).contains("test@example.com");
        assertThat(toString).contains("UserDTO");
    }

    @Test
    @DisplayName("ToString - Null Fields")
    void toString_NullFields() {
        UserDTO dto = new UserDTO(null, null, null, null);

        String toString = dto.toString();

        assertThat(toString).contains("null");
        assertThat(toString).contains("UserDTO");
    }

    @Test
    @DisplayName("CanEqual - Same Class")
    void canEqual_SameClass() {
        UserDTO dto1 = new UserDTO(1L, "user", "pass", "user@test.com");
        UserDTO dto2 = new UserDTO(2L, "user2", "pass2", "user2@test.com");

        assertThat(dto1.canEqual(dto2)).isTrue();
    }

    @Test
    @DisplayName("CanEqual - Different Class")
    void canEqual_DifferentClass() {
        UserDTO dto = new UserDTO(1L, "user", "pass", "user@test.com");
        String differentClass = "string";

        assertThat(dto.canEqual(differentClass)).isFalse();
    }

    @Test
    @DisplayName("Setters and Getters - All Fields")
    void settersAndGetters_AllFields() {
        UserDTO dto = new UserDTO();

        dto.setId(5L);
        dto.setUsername("newuser");
        dto.setPassword("newpass");
        dto.setEmail("new@example.com");

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getUsername()).isEqualTo("newuser");
        assertThat(dto.getPassword()).isEqualTo("newpass");
        assertThat(dto.getEmail()).isEqualTo("new@example.com");
    }
}
