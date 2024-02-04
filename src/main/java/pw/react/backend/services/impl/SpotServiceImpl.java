package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.Spot.SpotCreationDto;
import pw.react.backend.dto.Spot.SpotInfoDto;
import pw.react.backend.mapper.SpotMapper;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.Reservation;
import pw.react.backend.models.Spot;
import pw.react.backend.repository.CarParkRepository;
import pw.react.backend.repository.ReservationRepository;
import pw.react.backend.repository.SpotRepository;
import pw.react.backend.services.SpotService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotServiceImpl implements SpotService {
    private final SpotRepository spotRepository;
    private final CarParkRepository carParkRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public SpotServiceImpl(SpotRepository spotRepository, CarParkRepository carParkRepository,
                           ReservationRepository reservationRepository){
        this.spotRepository = spotRepository;
        this.carParkRepository = carParkRepository;
        this.reservationRepository = reservationRepository;
    }


    @Override
    public void createSpot(SpotCreationDto spotCreationDto) {
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

    @Override
    public PageResponse<SpotInfoDto> getSpotsByCarParkId(Long carParkId, Pageable pageable) {
        Page<Spot> spotsPage = spotRepository.findByCarParkId(carParkId, pageable);

        List<SpotInfoDto> spotInfoDtos = spotsPage.getContent().stream()
                .map(SpotMapper::mapToSpotInfoDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                spotInfoDtos,
                spotsPage.getTotalElements(),
                spotsPage.getTotalPages()
        );
    }

    @Override
    public Spot findAvailableSpot(CarPark carPark, LocalDateTime startDate, LocalDateTime endDate) {
        List<Spot> allSpots = spotRepository.findByCarParkId(carPark.getId());

        for (Spot spot : allSpots) {
            if (isSpotAvailable(spot, startDate, endDate)) {
                return spot;
            }
        }

        return null;
    }

    @Override
    public boolean isSpotAvailable(Spot spot, LocalDateTime startDate, LocalDateTime endDate) {
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                spot.getId(), startDate, endDate);

        return overlappingReservations.isEmpty();
    }
}
