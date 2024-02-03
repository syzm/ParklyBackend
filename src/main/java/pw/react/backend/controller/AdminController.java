package pw.react.backend.controller;

import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pw.react.backend.dto.CarPark.CarParkCreationDto;
import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.exceptions.CarParkValidationException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.UserService;

@RestController
@RequestMapping(path = AdminController.ADMIN_PATH)
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    static final String ADMIN_PATH = "/admin";
    private final UserService userService;
    private final CarParkService carParkService;
    public AdminController(UserService userService, CarParkService carParkService) {
        this.userService = userService;
        this.carParkService = carParkService;
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
    @PostMapping("/car_park/add")
    public ResponseEntity<Void> createParking(@RequestBody CarParkCreationDto carParkCreationDto) {
        try {
            carParkService.CreateCarPark(carParkCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CarParkValidationException ex) {
            log.error("Car park validation failed: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Error creating car park: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
