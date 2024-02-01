package pw.react.backend.services;

import pw.react.backend.dto.UserDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.User;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id) throws ResourceNotFoundException;
    UserDto updateUser(Long id, UserDto updatedUser);
}
