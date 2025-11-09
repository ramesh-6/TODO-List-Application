package userservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import userservice.entity.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

    @Test
    @DisplayName("findByUsername - User Exists")
    void findByUsername_UserExists() {
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByUsername("testuser");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("findByUsername - User Does Not Exist")
    void findByUsername_UserDoesNotExist() {
        Optional<User> found = userRepository.findByUsername("nonexistent");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("save - New User")
    void save_NewUser() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("findById - User Exists")
    void findById_UserExists() {
        User saved = entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("findById - User Does Not Exist")
    void findById_UserDoesNotExist() {
        Optional<User> found = userRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll - Multiple Users")
    void findAll_MultipleUsers() {
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");

        entityManager.persist(testUser);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername).containsExactlyInAnyOrder("testuser", "user2");
    }

    @Test
    @DisplayName("findAll - No Users")
    void findAll_NoUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("delete - User Exists")
    void delete_UserExists() {
        User saved = entityManager.persistAndFlush(testUser);

        userRepository.delete(saved);
        entityManager.flush();

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("update - User Fields")
    void update_UserFields() {
        User saved = entityManager.persistAndFlush(testUser);

        saved.setUsername("updateduser");
        saved.setEmail("updated@example.com");
        userRepository.save(saved);
        entityManager.flush();

        User updated = entityManager.find(User.class, saved.getId());
        assertThat(updated.getUsername()).isEqualTo("updateduser");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    @DisplayName("findByUsername - Case Sensitive")
    void findByUsername_CaseSensitive() {
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByUsername("TESTUSER");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("existsById - User Exists")
    void existsById_UserExists() {
        User saved = entityManager.persistAndFlush(testUser);

        boolean exists = userRepository.existsById(saved.getId());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById - User Does Not Exist")
    void existsById_UserDoesNotExist() {
        boolean exists = userRepository.existsById(999L);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("count - Users")
    void count_Users() {
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");

        entityManager.persist(testUser);
        entityManager.persist(user2);
        entityManager.flush();

        long count = userRepository.count();

        assertThat(count).isEqualTo(2);
    }
}
