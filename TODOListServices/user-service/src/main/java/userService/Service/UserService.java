package userService.Service;

import userService.DTO.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> createUsers(List<UserDTO> userDTOS);

    Boolean isValidUser(Long id);

    UserDTO updateUser(UserDTO userDTO);

    List<UserDTO> getAllUser();

    UserDTO getUserById(long id);

    void deleteUser(long id);

    UserDTO findByUsername(String username);
}
