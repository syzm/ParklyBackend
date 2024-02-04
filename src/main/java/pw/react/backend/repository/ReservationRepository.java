package pw.react.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pw.react.backend.enums.ReservationStatus;
import pw.react.backend.models.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.spot.id = :spotId " +
            "AND ((:startDate BETWEEN r.startDate AND r.endDate) OR " +
            "(:endDate BETWEEN r.startDate AND r.endDate) OR " +
            "(:startDate <= r.startDate AND :endDate >= r.endDate))")
    List<Reservation> findOverlappingReservations(
            @Param("spotId") Long spotId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    Page<Reservation> findByUser_Id(Long userId, Pageable pageable);

    Page<Reservation> findBySpot_CarPark_Id(Long carParkId, Pageable pageable);

    List<Reservation> findByEndDateBeforeAndStatus(LocalDateTime endDate, ReservationStatus status);
}
