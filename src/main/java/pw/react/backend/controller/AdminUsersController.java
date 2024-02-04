package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.Spot.SpotCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.Spot;
import pw.react.backend.services.UserService;

import java.util.List;

@RestController
@RequestMapping(path = AdminUsersController.PATH)
public class AdminUsersController {
    private static final Logger log = LoggerFactory.getLogger(AdminUsersController.class);
    static final String PATH = "/admin/users";
    private final UserService userService;

    public AdminUsersController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<CustomerInfoDto>> findCustomersByParameters(
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CustomerInfoDto> customersPageResponse = userService.findCustomersByParameters(
                firstName, lastName, email, pageable
        );
        return new ResponseEntity<>(customersPageResponse, HttpStatus.OK);
    }
}
