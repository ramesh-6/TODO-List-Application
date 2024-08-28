package TODOlist.app.Service;

import TODOlist.app.Entity.Task;
import TODOlist.app.Entity.User;
import TODOlist.app.Exception.UserNotFoundException;
import TODOlist.app.Repository.TaskRepository;
import TODOlist.app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TODOServiceImpl implements TODOService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public List<Task> getAllTask() {
        return this.taskRepository.findAll();
    }
    @Override
    public User getUserById(long UserId) {
        Optional<User> userDB = this.userRepository.findById(UserId);

        if (userDB.isPresent()) {
            return userDB.get();
        } else {
            throw new UserNotFoundException("Record not found with id : " + UserId);
        }
    }

    @Override
    public Task getTaskById(long taskID) {
        Optional<Task> taskDB = this.taskRepository.findById(taskID);

        if (taskDB.isPresent()) {
            return taskDB.get();
        } else {
            throw new UserNotFoundException("Record not found with id : " + taskID);
        }
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<User> createUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public List<Task> createTasks(List<Task> tasks) {
        return taskRepository.saveAll(tasks);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> userDB = this.userRepository.findById(user.getId());
        if (userDB.isPresent()) {
            User userUpdate = userDB.get();
            userUpdate.setId(user.getId());
            userUpdate.setUsername(user.getUsername());
            userUpdate.setPassword(user.getPassword());
            userUpdate.setEmail(user.getEmail());
            userRepository.save(userUpdate);
            return userUpdate;
        } else {
            throw new UserNotFoundException("Record not found with id : " + user.getId());
        }
    }

    @Override
    public Task updateTask(Task task) {
        Optional<Task> taskDB = this.taskRepository.findById(task.getId());
        if (taskDB.isPresent()) {
            Task taskUpdate = taskDB.get();
            taskUpdate.setId(task.getId());
            taskUpdate.setTitle(task.getTitle());
            taskUpdate.setDescription(task.getDescription());
            taskUpdate.setCompleted(task.getCompleted());
            taskUpdate.setUser(task.getUser());
            taskRepository.save(taskUpdate);
            return taskUpdate;
        } else {
            throw new UserNotFoundException("Record not found with id : " + task.getId());
        }
    }

    @Override
    public void deleteUser(long userId) {
        Optional<User> userDB = this.userRepository.findById(userId);

        if (userDB.isPresent()) {
            this.userRepository.delete(userDB.get());
        } else {
            throw new UserNotFoundException("Record not found with id : " + userId);
        }
    }

    @Override
    public void deleteTask(long taskId) {
        Optional<Task> taskDB = this.taskRepository.findById(taskId);

        if (taskDB.isPresent()) {
            this.taskRepository.delete(taskDB.get());
        } else {
            throw new UserNotFoundException("Record not found with id : " + taskId);
        }
    }
}
