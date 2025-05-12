package taskService.Service;

import taskService.DTO.TaskDTO;

import java.util.List;

public interface Service {

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> createTasks(List<TaskDTO> taskDTOS);

    TaskDTO updateTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTask();

    TaskDTO getTaskById(long id);

    List<TaskDTO> getTasksByUserId(long id);

    void deleteTask(long id);

}
