package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.dto.User.CustomerPatchDto;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.User;
import pw.react.backend.services.UserService;

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
    public ResponseEntity<Void> createUser(@RequestBody CustomerCreationDto customerCreationDto) {
        try {
            userService.createCustomer(customerCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), USERS_PATH);
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<CustomerInfoDto> getCurrentUserInfo(@AuthenticationPrincipal User userDetails) {
        if (userDetails != null) {
            Long userId = userDetails.getId();
            CustomerInfoDto customerInfoDto = userService.getCustomerByUserId(userId);
            return new ResponseEntity<>(customerInfoDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal User userDetails,
                                             @RequestBody CustomerPatchDto updatedCustomer) {
        if (userDetails != null) {
            Long userId = userDetails.getId();
            try {
                userService.updateCustomer(userId, updatedCustomer);
                return ResponseEntity.ok("User updated successfully");
            } catch (Exception ex) {
                throw new UserValidationException(ex.getMessage(), USERS_PATH);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmailAvailability(@RequestBody String email) {
        try {
            if (userService.isEmailUsed(email)) {
                return ResponseEntity.ok("Email is already used");
            } else {
                return ResponseEntity.ok("Email is available");
            }
        } catch (Exception ex) {
            log.error("Error checking email availability: {}", ex.getMessage());
            throw new UserValidationException(ex.getMessage(), USERS_PATH);
        }
    }

}
