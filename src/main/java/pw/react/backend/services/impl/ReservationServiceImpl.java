package pw.react.backend.services.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.Reservation;
import pw.react.backend.models.Spot;
import pw.react.backend.models.User;
import pw.react.backend.repository.CarParkRepository;
import pw.react.backend.repository.ReservationRepository;
import pw.react.backend.repository.SpotRepository;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.ReservationService;
import pw.react.backend.services.SpotService;
import pw.react.backend.services.UserService;

import java.time.temporal.ChronoUnit;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final CarParkService carParkService;

    private final SpotService spotService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, UserService userService,
                                  CarParkService carParkService, SpotService spotService){
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.carParkService = carParkService;
        this.spotService = spotService;
    }


    @Override
    public void makeReservation(ReservationCreationDto reservationCreationDto, Long userId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        CarPark carPark = carParkService.getCarParkById(reservationCreationDto.getCarParkId());

        if (carPark == null) {
            throw new ResourceNotFoundException("Car Park not found with ID: " + reservationCreationDto.getCarParkId());
        }

        Spot availableSpot =
                spotService.findAvailableSpot(carPark, reservationCreationDto.getStartDate(), reservationCreationDto.getEndDate());

        if (availableSpot == null) {
            throw new RuntimeException("No available spots found in the specified time range.");
        }

        long daysBetween = ChronoUnit.DAYS.between(reservationCreationDto.getStartDate(), reservationCreationDto.getEndDate());
        double cost = daysBetween * carPark.getDailyCost();

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDate(reservationCreationDto.getStartDate());
        reservation.setEndDate(reservationCreationDto.getEndDate());
        reservation.setCanceled(false);
        reservation.setSpot(availableSpot);
        Long externalUserId = reservationCreationDto.getExternalUserId();
        if (externalUserId != null && externalUserId >= 0)
            reservation.setExternalUserId(externalUserId);
        reservation.setCostEuros(cost);

        reservationRepository.save(reservation);
    }
}
