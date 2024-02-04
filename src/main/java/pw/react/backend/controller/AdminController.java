package pw.react.backend.controller;

import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.CarPark.CarParkCreationDto;
import pw.react.backend.dto.CarPark.CarParkInfoDto;
import pw.react.backend.dto.CarPark.CarParkPatchDto;
import pw.react.backend.dto.Spot.SpotCreationDto;
import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.exceptions.CarParkValidationException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.Spot;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.UserService;
import pw.react.backend.services.ReservationService;
import pw.react.backend.enums.ReservationStatus;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = AdminController.ADMIN_PATH)
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    static final String ADMIN_PATH = "/admin";
    private final UserService userService;
    private final ReservationService reservationService;

    public AdminController(UserService userService,
                           ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/reservations")
    public ResponseEntity<PageResponse<ReservationInfoDto>> findReservationsByParameters(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "spotId", required = false) Long spotId,
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "status", required = false) ReservationStatus status,
            @RequestParam(name = "externalUserId", required = false) Long externalUserId,
            @RequestParam(name = "costMin", required = false) Double costMin,
            @RequestParam(name = "costMax", required = false) Double costMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ReservationInfoDto> reservationsPageResponse = reservationService.getByParameters(
                userId, spotId, startDate, endDate, status, externalUserId, costMin, costMax, pageable
        );
        return new ResponseEntity<>(reservationsPageResponse, HttpStatus.OK);
    }
}
