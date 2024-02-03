package pw.react.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pw.react.backend.dto.CarPark.CarParkCreationDto;
import pw.react.backend.dto.CarPark.CarParkInfoDto;
import pw.react.backend.dto.CarPark.CarParkPatchDto;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.PageResponse;

import java.util.List;

public interface CarParkService {
    void CreateCarPark(CarParkCreationDto carParkCreationDto);

    PageResponse<CarParkInfoDto> getFilteredCarParks(Double dailyCostMin, Double dailyCostMax,
                                     String iso3166Name, String cityName,
                                     String streetName, Boolean isActive,
                                     Pageable pageable);

    void patchCarPark(Long carParkId, CarParkPatchDto carParkPatchDto);
}
