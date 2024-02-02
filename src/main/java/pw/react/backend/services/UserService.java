package pw.react.backend.services;

import pw.react.backend.dto.UserCreationDto;
import pw.react.backend.dto.UserDto;
import pw.react.backend.dto.UserPatchDto;
import pw.react.backend.exceptions.ResourceNotFoundException;

public interface UserService {
    void createUser(UserCreationDto userDto);
    UserDto getUserById(Long id) throws ResourceNotFoundException;
    void updateUser(Long id, UserPatchDto updatedUser);
}