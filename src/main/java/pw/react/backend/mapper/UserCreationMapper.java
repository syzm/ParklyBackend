package pw.react.backend.mapper;

import pw.react.backend.dto.UserCreationDto;
import pw.react.backend.models.User;

public class UserCreationMapper {
    public static User mapToUser(UserCreationDto userCreationDto) {
        User user = new User();
        user.setEmail(userCreationDto.getEmail());
        user.setPassword(userCreationDto.getPassword());
        user.setFirstName(userCreationDto.getFirstName());
        user.setLastName(userCreationDto.getLastName());
        user.setBirthDate(userCreationDto.getBirthDate());
        return user;
    }
}
