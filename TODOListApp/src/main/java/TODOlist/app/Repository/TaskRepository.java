package TODOlist.app.Repository;

import TODOlist.app.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByuser_id(Long id);

//    List<Task> findTasksByUsername(User user);
}