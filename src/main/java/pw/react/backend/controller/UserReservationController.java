package pw.react.backend.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UnauthorizedException;
import pw.react.backend.models.PageResponse;
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
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<Double> createReservation(@RequestBody @Valid ReservationCreationDto reservationDto,
                                                    @AuthenticationPrincipal User userDetails) {
        try {
            Long userId = userDetails.getId();
            Double cost = reservationService.makeReservation(reservationDto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cost);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<PageResponse<ReservationInfoDto>> getUserReservations(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Long userId = userDetails.getId();
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
            PageResponse<ReservationInfoDto> userReservations = reservationService.getUserReservations(userId, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(userReservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/officely")
    public ResponseEntity<PageResponse<ReservationInfoDto>> getExternalUserReservations(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long externalUserId) {
        try {
            Long userId = userDetails.getId();
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
            PageResponse<ReservationInfoDto> userReservations = reservationService.getExternalUserReservations(userId, externalUserId, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(userReservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId,
                                                    @AuthenticationPrincipal User userDetails) {
        try {
            Long userId = userDetails.getId();
            reservationService.userReservationCancel(reservationId, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Reservation canceled successfully");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to cancel this reservation.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found with ID: " + reservationId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling reservation");
        }
    }


}
