package pw.react.backend.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.dto.Reservation.ReservationPatchDto;
import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.PageResponse;
import pw.react.backend.services.ReservationService;
import pw.react.backend.services.UserService;

@RestController
@RequestMapping(path = AdminReservationController.ADMIN_PATH)
public class AdminReservationController {

    private static final Logger log = LoggerFactory.getLogger(AdminReservationController.class);
    static final String ADMIN_PATH = "/admin/reservation";
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<PageResponse<ReservationInfoDto>> getUserReservations(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
            PageResponse<ReservationInfoDto> userReservations = reservationService.getUserReservations(userId, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(userReservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/car_park")
    public ResponseEntity<PageResponse<ReservationInfoDto>> getCarParkReservations(
            @RequestParam Long carParkId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
            PageResponse<ReservationInfoDto> carParkReservations =
                    reservationService.getCarParkReservations(carParkId, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(carParkReservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{reservationId}")
    public ResponseEntity<String> patchReservation(@PathVariable Long reservationId,
                                                   @RequestBody @Valid ReservationPatchDto reservationPatchDto) {
        try {
            reservationService.patchReservation(reservationId, reservationPatchDto);
            return ResponseEntity.status(HttpStatus.OK).body("Reservation patched successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found with ID: " + reservationId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error patching reservation");
        }
    }

}
