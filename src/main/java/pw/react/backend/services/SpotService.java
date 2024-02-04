package pw.react.backend.services;

import org.springframework.data.domain.Pageable;
import pw.react.backend.dto.Spot.SpotCreationDto;
import pw.react.backend.dto.Spot.SpotInfoDto;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.Spot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SpotService {
    void createSpot(SpotCreationDto spotCreationDto);
    PageResponse<SpotInfoDto> getSpotsByCarParkId(Long carParkId, Pageable pageable);
    Spot findAvailableSpot(CarPark carPark, LocalDateTime startDate, LocalDateTime endDate);
    boolean isSpotAvailable(Spot spot, LocalDateTime startDate, LocalDateTime endDate);
}
