package pw.react.backend.services;

import org.springframework.data.domain.Pageable;
import pw.react.backend.dto.CarPark.CarParkCreationDto;
import pw.react.backend.dto.CarPark.CarParkInfoDto;
import pw.react.backend.dto.CarPark.CarParkPatchDto;
import pw.react.backend.models.PageResponse;

import java.time.LocalDateTime;

public interface CarParkService {
    void createCarPark(CarParkCreationDto carParkCreationDto);

    PageResponse<CarParkInfoDto> getFilteredCarParks(Double dailyCostMin, Double dailyCostMax,
                                     String iso3166Name, String cityName,
                                     String streetName, Boolean isActive,
                                     Pageable pageable);

    PageResponse<CarParkInfoDto> findCarParksForUser(String countryName,
                                                   String cityName,
                                                   LocalDateTime startDateTime,
                                                   LocalDateTime endDateTime,
                                                   Double dailyCostMin,
                                                   Double dailyCostMax,
                                                   double searchLatitude,
                                                   double searchLongitude,
                                                   double searchRadius,
                                                   Pageable pageable);

    void patchCarPark(Long carParkId, CarParkPatchDto carParkPatchDto);
}
