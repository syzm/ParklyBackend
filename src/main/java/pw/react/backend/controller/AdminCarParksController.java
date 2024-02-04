package pw.react.backend.controller;

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
import pw.react.backend.exceptions.CarParkValidationException;
import pw.react.backend.models.PageResponse;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.ReservationService;

@RestController
@RequestMapping(path = AdminCarParksController.PATH)
public class AdminCarParksController {

    private static final Logger log = LoggerFactory.getLogger(AdminCarParksController.class);
    static final String PATH = "/admin/car_park";
    private final CarParkService carParkService;
    private final ReservationService reservationService;

    public AdminCarParksController(CarParkService carParkService, ReservationService reservationService) {
        this.carParkService = carParkService;
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createParking(@RequestBody CarParkCreationDto carParkCreationDto) {
        try {
            carParkService.createCarPark(carParkCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CarParkValidationException ex) {
            log.error("Car park validation failed: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Error creating car park: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<CarParkInfoDto>> getFilteredCarParks(
            @RequestParam(name = "dailyCostMin", required = false) Double dailyCostMin,
            @RequestParam(name = "dailyCostMax", required = false) Double dailyCostMax,
            @RequestParam(name = "iso3166Name", required = false) String iso3166Name,
            @RequestParam(name = "cityName", required = false) String cityName,
            @RequestParam(name = "streetName", required = false) String streetName,
            @RequestParam(name = "isActive", required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CarParkInfoDto> pageResponse = carParkService.getFilteredCarParks(
                dailyCostMin, dailyCostMax, iso3166Name, cityName, streetName, isActive, pageable);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{carParkId}")
    public ResponseEntity<Void> patchCarPark(
            @PathVariable Long carParkId,
            @RequestBody CarParkPatchDto carParkPatchDto
    ) {
        carParkService.patchCarPark(carParkId, carParkPatchDto);
        if (!carParkPatchDto.isActive()) {
            reservationService.deactivateReservations(carParkId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<Long> getCarParkCount() {
        long carParkCount = carParkService.getCarParkCount();
        return new ResponseEntity<>(carParkCount, HttpStatus.OK);
    }

}
