package pw.react.backend.services;

import pw.react.backend.dto.UserCreationDto;
import pw.react.backend.dto.UserDto;
import pw.react.backend.dto.UserPatchDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.User;

public interface UserService {
    boolean createUser(UserCreationDto userDto);
    UserDto getUserById(Long id) throws ResourceNotFoundException;
    boolean updateUser(Long id, UserPatchDto updatedUser);
}