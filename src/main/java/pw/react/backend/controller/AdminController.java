package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.PageResponse;
import pw.react.backend.services.CustomerService;
import pw.react.backend.services.UserService;

import java.util.List;

@RestController
@RequestMapping(path = AdminController.ADMIN_PATH)
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    static final String ADMIN_PATH = "/admin";
    private final UserService userService;
    private final CustomerService customerService;

    public AdminController(UserService userService, CustomerService customerService) {
        this.userService = userService;
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createAdmin(@RequestBody AdminCreationDto adminCreationDto) {
        try {
            userService.createAdmin(adminCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), ADMIN_PATH);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<PageResponse<CustomerInfoDto>> findCustomersByParameters(
            @RequestParam(name = "userId", required = false, defaultValue = "0") long userId,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if(userId != 0) {
            CustomerInfoDto customer = userService.getCustomerByUserId(userId);
            var pageResponse = new PageResponse<>(List.of(customer), 1, 1);
            return new ResponseEntity<>(pageResponse, HttpStatus.OK);
        }


        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CustomerInfoDto> customersPageResponse = userService.findCustomersByParameters(
                firstName, lastName, email, pageable
        );
        return new ResponseEntity<>(customersPageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/customer_count")
    public ResponseEntity<Long> getCustomerCount() {
        long userCount = customerService.getCustomerCount();
        return new ResponseEntity<>(userCount, HttpStatus.OK);
    }
}
