package pw.react.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.enums.ReservationStatus;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.*;
import pw.react.backend.repository.ReservationRepository;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.ReservationService;
import pw.react.backend.services.SpotService;
import pw.react.backend.services.UserService;

import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

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
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setSpot(availableSpot);
        Long externalUserId = reservationCreationDto.getExternalUserId();
        if (externalUserId != null && externalUserId >= 0)
            reservation.setExternalUserId(externalUserId);
        reservation.setCost(cost);

        reservationRepository.save(reservation);
    }

    @Override
    public ReservationInfoDto returnReservationInfo(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        return new ReservationInfoDto(reservation);
    }

    @Override
    public PageResponse<ReservationInfoDto> getUserReservations(Long userId, Pageable pageable) {
        Page<Reservation> userReservations = reservationRepository.findByUser_Id(userId, pageable);
        return createReservationPageResponse(userReservations);
    }

    @Override
    public PageResponse<ReservationInfoDto> getCarParkReservations(Long carParkId, Pageable pageable) {
        Page<Reservation> carParkReservations = reservationRepository.findBySpot_CarPark_Id(carParkId, pageable);
        return createReservationPageResponse(carParkReservations);
    }


    private PageResponse<ReservationInfoDto> createReservationPageResponse(Page<Reservation> reservationsPage) {
        return new PageResponse<>(
                reservationsPage.getContent().stream()
                        .map(ReservationInfoDto::new)
                        .collect(Collectors.toList()),
                reservationsPage.getTotalElements(),
                reservationsPage.getTotalPages()
        );
    }
}
