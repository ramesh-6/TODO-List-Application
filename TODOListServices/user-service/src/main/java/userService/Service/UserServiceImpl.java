package userService.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import userService.DTO.UserDTO;
import userService.DTO.UserMapper;
import userService.Entity.User;
import userService.Exception.UserNotFoundException;
import userService.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user: {}",userDTO);
        User user = UserMapper.convertToEntity(userDTO);
        return UserMapper.convertToDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> createUsers(List<UserDTO> userDTOS) {
        logger.info("Creating users: {}",userDTOS);
        List<User> users = userDTOS.stream().map(UserMapper::convertToEntity).toList();
        return userRepository.saveAll(users).stream().map(UserMapper::convertToDTO).toList();
    }

    @Override
    public Boolean isValidUser(Long id){
        logger.info("validating user with ID: {}",id);
        Optional<User> userDB = userRepository.findById(id);
        if (userDB.isPresent()) {
            return userRepository.existsById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        logger.info("Updating user: {}",userDTO);
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
            throw new UserNotFoundException(userDTO.getId());
        }
    }

    @Override
    public List<UserDTO> getAllUser() {
        logger.info("Getting all users");
        return userRepository.findAll().stream().map(UserMapper::convertToDTO).toList();
    }

    @Override
    public UserDTO getUserById(long userId) {
        logger.info("Getting user by ID: {}",userId);
        Optional<User> userDB = userRepository.findById(userId);

        if (userDB.isPresent()) {
            return UserMapper.convertToDTO(userDB.get());
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public void deleteUser(long userId) {
        logger.info("Deleting user by ID: {}",userId);
        Optional<User> userDB = userRepository.findById(userId);

        if (userDB.isPresent()) {
            this.userRepository.delete(userDB.get());
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public UserDTO findByUsername(String username) {
        logger.info("Getting user by username: {}",username);
        Optional<User> userDB = userRepository.findByUsername(username);

        if (userDB.isPresent()) {
            return UserMapper.convertToDTO(userDB.get());
        } else {
            throw new UserNotFoundException(userDB.get().getId());
        }
    }
}
