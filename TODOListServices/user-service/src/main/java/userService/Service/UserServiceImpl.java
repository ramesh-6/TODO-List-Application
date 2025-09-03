package userService.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userService.DTO.UserDTO;
import userService.DTO.UserMapper;
import userService.Entity.User;
import userService.Exception.DatabaseException;
import userService.Exception.NoUsersFoundException;
import userService.Exception.UserAlreadyExistException;
import userService.Exception.UserNotFoundException;
import userService.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user: {}", userDTO);
        try {
            Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistException("User already exists with username: " + userDTO.getUsername());
            }

            User user = UserMapper.convertToEntity(userDTO);
            user.setPassword(encoder.encode(user.getPassword()));
            return UserMapper.convertToDTO(userRepository.save(user));

        } catch (DataAccessException e) {
            logger.error("Database error while creating user: {}", userDTO.getUsername(), e);
            throw new DatabaseException("Failed to create user", e);
        }
    }


    @Override
    public Boolean isValidUser(Long id){
        logger.info("validating user with ID: {}",id);
        try{
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return true;
            } else {
                throw new UserNotFoundException("User not found with id: " + id);
            }
        } catch (DataAccessException e){
            logger.error("Database error while validating user: {}", id, e);
            throw new DatabaseException("Failed to validate user", e);
        }
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        logger.info("Updating user: {}",userDTO);
        try{
            Optional<User> userDB = userRepository.findById(userDTO.getId());
            if (userDB.isPresent()) {
                User userUpdate = userDB.get();
                userUpdate.setId(userDTO.getId());
                userUpdate.setUsername(userDTO.getUsername());
                userUpdate.setPassword(userDTO.getPassword());
                userUpdate.setEmail(userDTO.getEmail());
                userRepository.save(userUpdate);
                return UserMapper.convertToDTO(userUpdate);
            } else {
                throw new UserNotFoundException("User already exists with id: " + userDTO.getId());
            }
        } catch (DataAccessException e){
            logger.error("Database error while updating user: {}", userDTO.getId(), e);
            throw new DatabaseException("Failed to update user", e);
        }
    }

    @Override
    public List<UserDTO> getAllUser() {
        logger.info("Getting all users");
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                throw new NoUsersFoundException("No users found in the system");
            }
            return users.stream()
                    .map(UserMapper::convertToDTO)
                    .toList();
        } catch (DataAccessException e) {
            logger.error("Database error while fetching all users", e);
            throw new DatabaseException("Failed to retrieve users", e);
        }
    }


    @Override
    public UserDTO getUserById(long userId) {
        logger.info("Getting user by ID: {}",userId);
        try{
            Optional<User> userDB = userRepository.findById(userId);
            if (userDB.isPresent()) {
                return UserMapper.convertToDTO(userDB.get());
            } else {
                throw new UserNotFoundException("User already exists with id: " + userId);
            }
        } catch (DataAccessException e){
            logger.error("Database error while fetching user by ID", e);
            throw new DatabaseException("Failed to retrieve user by ID", e);
        }
    }

    @Override
    public UserDTO findByUsername(String username) {
        logger.info("Getting user by username: {}",username);
        try{
            Optional<User> existingUser = userRepository.findByUsername(username);

            if (existingUser.isPresent()) {
                return UserMapper.convertToDTO(existingUser.get());
            } else {
                throw new UserNotFoundException(username);
            }
        } catch (DataAccessException e){
            logger.error("Database error while finding user by username", e);
            throw new DatabaseException("Failed to retrieve user by username", e);
        }
    }

    @Override
    public void deleteUser(long userId) {
        logger.info("Deleting user by ID: {}",userId);
        try{
            Optional<User> userDB = userRepository.findById(userId);
            if (userDB.isPresent()) {
                this.userRepository.delete(userDB.get());
            } else {
                throw new UserNotFoundException("User already exists with id: " + userId);
            }
        } catch (DataAccessException e){
            logger.error("Database error while deleting user", e);
            throw new DatabaseException("Failed to delete user", e);
        }
    }

}
