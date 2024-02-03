package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.Spot.SpotCreationDto;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.Spot;
import pw.react.backend.repository.CarParkRepository;
import pw.react.backend.repository.SpotRepository;
import pw.react.backend.services.SpotService;

@Service
public class SpotServiceImpl implements SpotService {
    private final SpotRepository spotRepository;
    private final CarParkRepository carParkRepository;

    @Autowired
    public SpotServiceImpl(SpotRepository spotRepository, CarParkRepository carParkRepository){
        this.spotRepository = spotRepository;
        this.carParkRepository = carParkRepository;
    }


    @Override
    public void CreateSpot(SpotCreationDto spotCreationDto) {
        Long carParkId = spotCreationDto.getCarParkId();
        CarPark carPark = carParkRepository.findById(carParkId)
                .orElseThrow(() -> new RuntimeException("Car park not found"));

        Spot newSpot = new Spot();
        newSpot.setCarPark(carPark);
        String spotName = spotCreationDto.getName();
        if (spotName != null) {
            newSpot.setName(spotName);
        }
        spotRepository.save(newSpot);
    }
}
