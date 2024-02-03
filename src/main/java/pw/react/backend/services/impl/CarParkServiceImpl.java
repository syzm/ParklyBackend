package pw.react.backend.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.CarPark.CarParkCreationDto;
import pw.react.backend.dto.CarPark.CarParkInfoDto;
import pw.react.backend.dto.CarPark.CarParkPatchDto;
import pw.react.backend.mapper.CarParkMapper;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.City;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.Street;
import pw.react.backend.repository.CarParkRepository;
import pw.react.backend.repository.StreetRepository;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.StreetService;
import pw.react.backend.utils.Utils;

import java.util.List;
import java.util.Optional;

@Service
public class CarParkServiceImpl implements CarParkService {
    private final CarParkRepository carParkRepository;
    private final StreetService streetService;
    @Autowired
    public CarParkServiceImpl(CarParkRepository carParkRepository, StreetService streetService) {
        this.carParkRepository = carParkRepository;
        this.streetService = streetService;
    }

    @Override
    public void createCarPark(CarParkCreationDto carParkCreationDto) {
        // Find or create Street and rest of address
        Street existingStreet = streetService.getOrCreateStreet(carParkCreationDto.getStreetName(),
                carParkCreationDto.getCityName(), carParkCreationDto.getIso3166Country());

        Optional<CarPark> existingCarPark = carParkRepository.findByBuildingNumberAndStreet_Id(
                carParkCreationDto.getBuildingNumber(),
                existingStreet.getId()
        );

        if (existingCarPark.isPresent()) {
            throw new RuntimeException("CarPark with given latitudes already exists.");
        }

        CarPark newCarPark = new CarPark();
        newCarPark.setStreet(existingStreet);
        newCarPark.setBuildingNumber(carParkCreationDto.getBuildingNumber());
        newCarPark.setLongitude(carParkCreationDto.getLongitude());
        newCarPark.setLatitude(carParkCreationDto.getLatitude());
        newCarPark.setPostalCode(carParkCreationDto.getPostalCode());
        newCarPark.setActive(carParkCreationDto.isActive());
        newCarPark.setDailyCost(carParkCreationDto.getDailyCost());

        carParkRepository.save(newCarPark);
    }

    @Override
    public PageResponse<CarParkInfoDto> getFilteredCarParks(Double dailyCostMin, Double dailyCostMax,
                                                            String iso3166Name, String cityName,
                                                            String streetName, Boolean isActive,
                                                            Pageable pageable) {
        Page<CarPark> filteredCarParks = carParkRepository.findByFilters(
                dailyCostMin, dailyCostMax, iso3166Name, cityName, streetName, isActive, pageable);

        List<CarParkInfoDto> carParks = filteredCarParks.map(CarParkMapper::mapToDto).getContent();
        long totalElements = filteredCarParks.getTotalElements();
        int totalPages = filteredCarParks.getTotalPages();

        return new PageResponse<>(carParks, totalElements, totalPages);
    }

    @Override
    public void patchCarPark(Long carParkId, CarParkPatchDto carParkPatchDto) {
        CarPark existingCarPark = carParkRepository.findById(carParkId)
                .orElseThrow(() -> new RuntimeException("Car Park not found with id: " + carParkId));
        BeanUtils.copyProperties(carParkPatchDto, existingCarPark, Utils.getNullPropertyNames(carParkPatchDto));
        carParkRepository.save(existingCarPark);
    }
}
