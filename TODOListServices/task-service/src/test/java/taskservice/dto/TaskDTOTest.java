package taskservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskDTO Tests")
class TaskDTOTest {

    @Test
    @DisplayName("NoArgsConstructor - Creates Empty Object")
    void noArgsConstructor_CreatesEmptyObject() {
        TaskDTO dto = new TaskDTO();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getTitle()).isNull();
        assertThat(dto.getDescription()).isNull();
        assertThat(dto.getUserID()).isNull();
        assertThat(dto.getCompleted()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor - Sets All Fields")
    void allArgsConstructor_SetsAllFields() {
        TaskDTO dto = new TaskDTO(1L, "Task", "Description", 100L, false);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Task");
        assertThat(dto.getDescription()).isEqualTo("Description");
        assertThat(dto.getUserID()).isEqualTo(100L);
        assertThat(dto.getCompleted()).isFalse();
    }

    @Test
    @DisplayName("Setters and Getters - All Fields")
    void settersAndGetters_AllFields() {
        TaskDTO dto = new TaskDTO();

        dto.setId(5L);
        dto.setTitle("New Task");
        dto.setDescription("New Description");
        dto.setUserID(200L);
        dto.setCompleted(true);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getTitle()).isEqualTo("New Task");
        assertThat(dto.getDescription()).isEqualTo("New Description");
        assertThat(dto.getUserID()).isEqualTo(200L);
        assertThat(dto.getCompleted()).isTrue();
    }

    @Test
    @DisplayName("Equals and HashCode - Same Values")
    void equalsAndHashCode_SameValues() {
        TaskDTO dto1 = new TaskDTO(1L, "Task", "Desc", 100L, false);
        TaskDTO dto2 = new TaskDTO(1L, "Task", "Desc", 100L, false);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("ToString - Contains All Fields")
    void toString_ContainsAllFields() {
        TaskDTO dto = new TaskDTO(1L, "Task", "Description", 100L, true);

        String toString = dto.toString();

        assertThat(toString).contains("1");
        assertThat(toString).contains("Task");
        assertThat(toString).contains("Description");
        assertThat(toString).contains("100");
        assertThat(toString).contains("true");
    }
}
