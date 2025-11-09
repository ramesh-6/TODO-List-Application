package taskservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import taskservice.entity.Task;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskMapper Tests")
class TaskMapperTest {

    @Test
    @DisplayName("convertToDTO - Success")
    void convertToDTO_Success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setUserID(100L);
        task.setCompleted(false);

        TaskDTO dto = TaskMapper.convertToDTO(task);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Test Task");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getUserID()).isEqualTo(100L);
        assertThat(dto.getCompleted()).isFalse();
    }

    @Test
    @DisplayName("convertToDTO - Null Task")
    void convertToDTO_NullTask() {
        TaskDTO dto = TaskMapper.convertToDTO(null);

        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("convertToEntity - Success")
    void convertToEntity_Success() {
        TaskDTO dto = new TaskDTO();
        dto.setId(1L);
        dto.setTitle("Test Task");
        dto.setDescription("Test Description");
        dto.setUserID(100L);
        dto.setCompleted(true);

        Task task = TaskMapper.convertToEntity(dto);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Test Description");
        assertThat(task.getUserID()).isEqualTo(100L);
        assertThat(task.getCompleted()).isTrue();
    }

    @Test
    @DisplayName("convertToEntity - Null DTO")
    void convertToEntity_NullDTO() {
        Task task = TaskMapper.convertToEntity(null);

        assertThat(task).isNull();
    }

    @Test
    @DisplayName("convertToDTO - Partial Data")
    void convertToDTO_PartialData() {
        Task task = new Task();
        task.setTitle("Only Title");

        TaskDTO dto = TaskMapper.convertToDTO(task);

        assertThat(dto).isNotNull();
        assertThat(dto.getTitle()).isEqualTo("Only Title");
        assertThat(dto.getDescription()).isNull();
        assertThat(dto.getUserID()).isNull();
        assertThat(dto.getCompleted()).isFalse();
    }

    @Test
    @DisplayName("Round Trip Conversion")
    void roundTripConversion() {
        TaskDTO originalDTO = new TaskDTO();
        originalDTO.setId(1L);
        originalDTO.setTitle("Round Trip");
        originalDTO.setDescription("Test");
        originalDTO.setUserID(100L);
        originalDTO.setCompleted(true);

        Task task = TaskMapper.convertToEntity(originalDTO);
        TaskDTO resultDTO = TaskMapper.convertToDTO(task);

        assertThat(resultDTO.getId()).isEqualTo(originalDTO.getId());
        assertThat(resultDTO.getTitle()).isEqualTo(originalDTO.getTitle());
        assertThat(resultDTO.getDescription()).isEqualTo(originalDTO.getDescription());
        assertThat(resultDTO.getUserID()).isEqualTo(originalDTO.getUserID());
        assertThat(resultDTO.getCompleted()).isEqualTo(originalDTO.getCompleted());
    }
}
