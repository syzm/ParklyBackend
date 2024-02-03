package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.CarPark.CarParkCreationDto;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.City;
import pw.react.backend.models.Street;
import pw.react.backend.repository.CarParkRepository;
import pw.react.backend.repository.StreetRepository;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.StreetService;

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
    public void CreateCarPark(CarParkCreationDto carParkCreationDto) {
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
        newCarPark.setDailyCost(carParkCreationDto.getDailyCost());

        carParkRepository.save(newCarPark);
    }
}
