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

    Page<Reservation> findByUser_IdAndExternalUserId(Long userId, Long externalUserId, Pageable pageable);

    Page<Reservation> findBySpot_CarPark_Id(Long carParkId, Pageable pageable);

    List<Reservation> findBySpot_CarPark_IdAndStatus(Long carParkId, ReservationStatus status);

    List<Reservation> findByEndDateBeforeAndStatus(LocalDateTime endDate, ReservationStatus status);

    @Query("SELECT r FROM Reservation r " +
            "WHERE (:userId is null or r.user.id = :userId) " +
            "AND (:spotId is null or r.spot.id = :spotId) " +
            "AND (:startDate is null or r.startDate >= :startDate) " +
            "AND (:endDate is null or r.endDate <= :endDate) " +
            "AND (:status is null or r.status = :status) " +
            "AND (:externalUserId is null or r.externalUserId = :externalUserId) " +
            "AND (:costMin is null or r.cost >= :costMin) " +
            "AND (:costMax is null or r.cost <= :costMax)")
    Page<Reservation> findByParameters(@Param("userId") Long userId,
                                       @Param("spotId") Long spotId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("status") ReservationStatus status,
                                       @Param("externalUserId") Long externalUserId,
                                       @Param("costMin") Double costMin,
                                       @Param("costMax") Double costMax,
                                       Pageable pageable);
}
