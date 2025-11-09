package taskservice.dto;

import taskservice.entity.Task;

public class TaskMapper {

    public static TaskDTO convertToDTO(Task task){
        if (task == null) {
            return null;
        }

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setUserID(task.getUserID());
        dto.setCompleted(task.getCompleted());
        return dto;
    }

    public  static Task convertToEntity(TaskDTO dto){
        if (dto == null) {
            return null;
        }

        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setUserID(dto.getUserID());
        task.setCompleted(dto.getCompleted());
        return task;
    }
}
