package pw.react.backend.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.User;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.ReservationService;

@RestController
@RequestMapping(path = UserReservationController.PATH)
public class UserReservationController {
    private static final Logger log = LoggerFactory.getLogger(UserReservationController.class);
    static final String PATH = "/user/reservation";
    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody @Valid ReservationCreationDto reservationDto,
                                                    @AuthenticationPrincipal User userDetails) {
        try {
            Long userId = userDetails.getId();
            reservationService.makeReservation(reservationDto, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body("Reservation created successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating reservation");
        }
    }

}
