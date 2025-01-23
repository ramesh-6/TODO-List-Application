package TODOlist.app.Service;

import TODOlist.app.Entity.Task;
import TODOlist.app.Entity.User;
import TODOlist.app.Exception.UserNotFoundException;
import TODOlist.app.Repository.TaskRepository;
import TODOlist.app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTask() {
        return this.taskRepository.findAll();
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
    public List<Task> getTasksByUserId(long userId) {
        Optional<User> userDB = this.userRepository.findById(userId);

        if (userDB.isPresent()) {
            List<Task> taskDB = this.taskRepository.findTasksByuser_id(userDB.get().getId());
            return taskDB;
        } else {
            throw new UserNotFoundException("User not found with id : " + userId);
        }
    }

    @Override
    public List<Task> getTasksByUsername(String username) {
        User user = this.userRepository.findByUsername(username);

        if (user != null) {
            List<Task> taskDB = this.taskRepository.findTasksByuser_id(user.getId());
            return taskDB;
        } else {
            throw new UserNotFoundException("User not found with username : " + username);
        }
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> createTasks(List<Task> tasks) {
        return taskRepository.saveAll(tasks);
    }

    @Override
    public Task updateTask(Task task) {
        Optional<Task> taskDB = this.taskRepository.findById(task.getId());
        if (taskDB.isPresent()) {
            Task taskUpdate = taskDB.get();
            taskUpdate.setId(task.getId());
            taskUpdate.setTitle(task.getTitle());
            taskUpdate.setCompleted(task.getCompleted());
            taskUpdate.setUser(task.getUser());
            taskRepository.save(taskUpdate);
            return taskUpdate;
        } else {
            throw new UserNotFoundException("Record not found with id : " + task.getId());
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
