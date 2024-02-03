package pw.react.backend.services;

import pw.react.backend.dto.CarPark.CarParkCreationDto;

public interface CarParkService {
    void CreateCarPark(CarParkCreationDto carParkCreationDto);
}
