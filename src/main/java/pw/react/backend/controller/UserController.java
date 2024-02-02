package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.UserDto;
import pw.react.backend.dto.UserPatchDto;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.User;
import pw.react.backend.services.UserService;
import pw.react.backend.dto.UserCreationDto;

@RestController
@RequestMapping(path = UserController.USERS_PATH)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    static final String USERS_PATH = "/users";
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserCreationDto userCreationDto) {
        try {
            userService.createUser(userCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), USERS_PATH);
        }
    }

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUserInfo(@AuthenticationPrincipal User userDetails) {
        if (userDetails != null) {
            Long userId = userDetails.getId();
            UserDto userDto = userService.getUserById(userId);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @PatchMapping
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal User userDetails,
                                             @RequestBody UserPatchDto updatedUser) {
        if (userDetails != null) {
            Long userId = userDetails.getId();
            try {
                userService.updateUser(userId, updatedUser);
                return ResponseEntity.ok("User updated successfully");
            } catch (Exception ex) {
                throw new UserValidationException(ex.getMessage(), USERS_PATH);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
