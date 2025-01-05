package TODOlist.app.Service;

import TODOlist.app.Entity.Task;
import TODOlist.app.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface TODOService extends UserDetailsService {

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

    List<Task> getTasksByUserId(long id);

    void deleteUser(long id);

    void deleteTask(long id);

    UserDetails loadUserByUsername(String username);
}
