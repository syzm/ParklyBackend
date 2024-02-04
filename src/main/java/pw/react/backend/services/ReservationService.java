package pw.react.backend.services;

import org.springframework.data.domain.Pageable;
import pw.react.backend.dto.Reservation.ReservationCreationDto;
import pw.react.backend.dto.Reservation.ReservationInfoDto;
import pw.react.backend.dto.Reservation.ReservationPatchDto;
import pw.react.backend.models.PageResponse;

public interface ReservationService {
    void makeReservation(ReservationCreationDto reservationCreationDto, Long userId);
    ReservationInfoDto returnReservationInfo(Long reservationId);

    PageResponse<ReservationInfoDto> getUserReservations(Long userId, Pageable pageable);

    PageResponse<ReservationInfoDto> getCarParkReservations(Long carParkId, Pageable pageable);

    void userReservationCancel(Long reservationId, Long userId);

    void patchReservation(Long reservationId, ReservationPatchDto reservationPatchDto);

}
