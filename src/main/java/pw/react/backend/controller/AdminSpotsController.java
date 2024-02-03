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
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.Spot;
import pw.react.backend.services.SpotService;

import java.util.List;

@RestController
@RequestMapping(path = AdminSpotsController.PATH)
public class AdminSpotsController {
    private static final Logger log = LoggerFactory.getLogger(AdminSpotsController.class);
    static final String PATH = "/admin/spot";
    private final SpotService spotService;

    public AdminSpotsController(SpotService spotService) {
        this.spotService = spotService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createSpot(@RequestBody SpotCreationDto spotCreationDto){
        try {
            spotService.createSpot(spotCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), PATH);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{carParkId}")
    public ResponseEntity<PageResponse<Spot>> getSpotsByCarParkId(
            @PathVariable Long carParkId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<Spot> spotsPageResponse = spotService.getSpotsByCarParkId(carParkId, pageable);
        return new ResponseEntity<>(spotsPageResponse, HttpStatus.OK);
    }

}
