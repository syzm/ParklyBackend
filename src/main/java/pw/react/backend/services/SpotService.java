package pw.react.backend.services;

import pw.react.backend.dto.Spot.SpotCreationDto;

public interface SpotService {
    void CreateSpot(SpotCreationDto spotCreationDto);
}
