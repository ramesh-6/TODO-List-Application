package TODOlist.app.Service;

import TODOlist.app.Entity.Task;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    List<Task> createTasks(List<Task> tasks);

    Task updateTask(Task task);

    List<Task> getAllTask();

    Task getTaskById(long id);

    List<Task> getTasksByUserId(long id);

    void deleteTask(long id);

}
