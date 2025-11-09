package taskservice.service;

import taskservice.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTask();

    TaskDTO getTaskById(long id);

    List<TaskDTO> getTasksByUserId(long id);

    void deleteTask(long id);

}
