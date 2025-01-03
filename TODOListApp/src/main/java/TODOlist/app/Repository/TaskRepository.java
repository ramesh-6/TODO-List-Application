package TODOlist.app.Repository;

import TODOlist.app.Entity.Task;
import TODOlist.app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByUser(Optional<User> user);
}