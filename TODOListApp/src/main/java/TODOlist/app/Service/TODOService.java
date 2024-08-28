package TODOlist.app.Service;

import TODOlist.app.Entity.Task;
import TODOlist.app.Entity.User;

import java.util.List;

public interface TODOService {

    User createUser(User user);

    Task createTask(Task task);

    List<User> createUsers(List<User> users);

    List<Task> createTasks(List<Task> tasks);

    User updateUser(User user);

    Task updateTask(Task task);

    List<User> getAllUser();

    List<Task> getAllTask();

    User getUserById(long id);

    Task getTaskById(long id);

    void deleteUser(long id);

    void deleteTask(long id);

}
