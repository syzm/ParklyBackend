package pw.react.backend.services;

import pw.react.backend.dto.Reservation.ReservationCreationDto;

public interface ReservationService {
    void makeReservation(ReservationCreationDto reservationCreationDto, Long userId);
}
