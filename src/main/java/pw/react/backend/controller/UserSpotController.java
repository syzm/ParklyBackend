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
import pw.react.backend.dto.CarPark.CarParksDistanceDto;
import pw.react.backend.exceptions.CarParkValidationException;
import pw.react.backend.models.PageResponse;
import pw.react.backend.services.CarParkService;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = UserSpotController.PATH)
public class UserSpotController {

    private static final Logger log = LoggerFactory.getLogger(UserSpotController.class);
    static final String PATH = "/user/spot";
    private final CarParkService carParkService;

    public UserSpotController(CarParkService carParkService) {
        this.carParkService = carParkService;
    }


    @GetMapping
    public ResponseEntity<PageResponse<CarParksDistanceDto>> getFilteredCarParks(
            @RequestParam(name = "countryName", required = true) String countryName,
            @RequestParam(name = "cityName", required = true) String cityName,
            @RequestParam(name = "startDateTime", required = true) LocalDateTime startDateTime,
            @RequestParam(name = "endDateTime", required = true) LocalDateTime endDateTime,
            @RequestParam(name = "dailyCostMin", required = false) Double dailyCostMin,
            @RequestParam(name = "dailyCostMax", required = false) Double dailyCostMax,
            @RequestParam(name = "searchLatitude", required = true) double searchLatitude,
            @RequestParam(name = "searchLongitude", required = true) double searchLongitude,
            @RequestParam(name = "searchRadius", required = true) double searchRadius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CarParksDistanceDto> pageResponse = carParkService.findCarParksForUser(
                countryName, cityName, startDateTime, endDateTime, dailyCostMin, dailyCostMax,
                searchLatitude, searchLongitude, searchRadius, pageable);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
