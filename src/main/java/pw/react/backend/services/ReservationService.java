package pw.react.backend.services;

import org.springframework.data.domain.Pageable;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.dto.Reservation.ReservationPatchDto;
import pw.react.backend.models.PageResponse;
import pw.react.backend.enums.ReservationStatus;

import java.time.LocalDateTime;

public interface ReservationService {
    Double makeReservation(ReservationCreationDto reservationCreationDto, Long userId);
    ReservationInfoDto returnReservationInfo(Long reservationId);

    PageResponse<ReservationInfoDto> getUserReservations(Long userId, Pageable pageable);

    PageResponse<ReservationInfoDto> getCarParkReservations(Long carParkId, Pageable pageable);

    void userReservationCancel(Long reservationId, Long userId);

    void patchReservation(Long reservationId, ReservationPatchDto reservationPatchDto);

    void refreshReservations();

    PageResponse<ReservationInfoDto> getByParameters(Long userId,
                                                      Long spotId,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate,
                                                      ReservationStatus status,
                                                      Long externalUserId,
                                                      Double costMin,
                                                      Double costMax,
                                                      Pageable pageable);

}
