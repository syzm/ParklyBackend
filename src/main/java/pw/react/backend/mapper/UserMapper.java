package pw.react.backend.mapper;

import pw.react.backend.dto.UserCreationDto;
import pw.react.backend.dto.UserDto;
import pw.react.backend.models.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate()
        );
    }
}
