package pw.react.backend.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.dto.Reservation.ReservationPatchDto;
import pw.react.backend.enums.ReservationStatus;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.*;
import pw.react.backend.repository.ReservationRepository;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.ReservationService;
import pw.react.backend.services.SpotService;
import pw.react.backend.services.UserService;
import pw.react.backend.mapper.ReservationMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pw.react.backend.utils.Utils.getNullPropertyNames;

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
    public Double makeReservation(ReservationCreationDto reservationCreationDto, Long userId) {
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
        double cost = (daysBetween + 1) * carPark.getDailyCost();

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

        return cost;
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

    @Override
    public void userReservationCancel(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this reservation.");
        }
        reservation.setStatus(ReservationStatus.CANCELED_BY_USER);
        reservationRepository.save(reservation);
    }

    @Override
    public void patchReservation(Long reservationId, ReservationPatchDto reservationPatchDto) {
        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        String newStatus = reservationPatchDto.getStatus();
        if (newStatus != null) {
            try {
                ReservationStatus status = ReservationStatus.valueOf(newStatus);
                existingReservation.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid reservation status: " + reservationPatchDto.getStatus());
            }
        }

        Double newCost = reservationPatchDto.getCost();
        if (newCost != null) {
            if (newCost < 0.0) {
                throw new IllegalArgumentException("Cost cannot be negative");
            }
            existingReservation.setCost(newCost);
        }

        reservationRepository.save(existingReservation);
    }

    @Override
    public void refreshReservations() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Reservation> activeReservations =
                reservationRepository.findByEndDateBeforeAndStatus(currentDate, ReservationStatus.ACTIVE);

        for (Reservation reservation : activeReservations) {
            reservation.setStatus(ReservationStatus.ARCHIVED);
        }

        reservationRepository.saveAll(activeReservations);
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

    @Override
    public PageResponse<ReservationInfoDto> getByParameters(Long userId,
                                                      Long spotId,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate,
                                                      ReservationStatus status,
                                                      Long externalUserId,
                                                      Double costMin,
                                                      Double costMax,
                                                      Pageable pageable) {

        Page<Reservation> filteredReservations = reservationRepository.findByParameters(
                userId, spotId, startDate, endDate, status,
                externalUserId, costMin, costMax, pageable);

        List<ReservationInfoDto> reservations = filteredReservations.stream()
                .map(ReservationMapper::mapToDto)
                .sorted(Comparator.comparingLong(ReservationInfoDto::getId))
                .collect(Collectors.toList());

        long totalElements = reservations.size();
        int totalPages = filteredReservations.getTotalPages();

        return new PageResponse<>(reservations, totalElements, totalPages);
    }
}
