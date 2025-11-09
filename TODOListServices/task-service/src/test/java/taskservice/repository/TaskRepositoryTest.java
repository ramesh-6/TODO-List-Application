package taskservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import taskservice.entity.Task;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("TaskRepository Tests")
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setUserID(100L);
        testTask.setCompleted(false);
    }

    @Test
    @DisplayName("findByUserID - Tasks Exist")
    void findByUserID_TasksExist() {
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setUserID(100L);
        task2.setCompleted(true);

        entityManager.persist(testTask);
        entityManager.persist(task2);
        entityManager.flush();

        List<Task> foundTasks = taskRepository.findByUserID(100L);

        assertThat(foundTasks).hasSize(2);
        assertThat(foundTasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Test Task", "Task 2");
        assertThat(foundTasks).extracting(Task::getUserID)
                .containsOnly(100L);
    }

    @Test
    @DisplayName("findByUserID - No Tasks Found")
    void findByUserID_NoTasksFound() {
        List<Task> foundTasks = taskRepository.findByUserID(999L);

        assertThat(foundTasks).isEmpty();
    }

    @Test
    @DisplayName("save - New Task")
    void save_NewTask() {
        Task savedTask = taskRepository.save(testTask);

        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
        assertThat(savedTask.getUserID()).isEqualTo(100L);
        assertThat(savedTask.getCompleted()).isFalse();
    }

    @Test
    @DisplayName("findById - Task Exists")
    void findById_TaskExists() {
        Task saved = entityManager.persistAndFlush(testTask);

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    @DisplayName("findById - Task Does Not Exist")
    void findById_TaskDoesNotExist() {
        Optional<Task> found = taskRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll - Multiple Tasks")
    void findAll_MultipleTasks() {
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setUserID(200L);
        task2.setCompleted(false);

        entityManager.persist(testTask);
        entityManager.persist(task2);
        entityManager.flush();

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Test Task", "Task 2");
    }

    @Test
    @DisplayName("delete - Task Exists")
    void delete_TaskExists() {
        Task saved = entityManager.persistAndFlush(testTask);

        taskRepository.delete(saved);
        entityManager.flush();

        Optional<Task> found = taskRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("update - Task Fields")
    void update_TaskFields() {
        Task saved = entityManager.persistAndFlush(testTask);

        saved.setTitle("Updated Task");
        saved.setCompleted(true);
        taskRepository.save(saved);
        entityManager.flush();

        Task updated = entityManager.find(Task.class, saved.getId());
        assertThat(updated.getTitle()).isEqualTo("Updated Task");
        assertThat(updated.getCompleted()).isTrue();
    }

    @Test
    @DisplayName("existsById - Task Exists")
    void existsById_TaskExists() {
        Task saved = entityManager.persistAndFlush(testTask);

        boolean exists = taskRepository.existsById(saved.getId());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById - Task Does Not Exist")
    void existsById_TaskDoesNotExist() {
        boolean exists = taskRepository.existsById(999L);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("count - Tasks")
    void count_Tasks() {
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setUserID(200L);
        task2.setCompleted(false);

        entityManager.persist(testTask);
        entityManager.persist(task2);
        entityManager.flush();

        long count = taskRepository.count();

        assertThat(count).isEqualTo(2);
    }
}
