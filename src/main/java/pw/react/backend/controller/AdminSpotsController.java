package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pw.react.backend.dto.Spot.SpotCreationDto;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.services.SpotService;

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
            spotService.CreateSpot(spotCreationDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), PATH);
        }
    }
}
