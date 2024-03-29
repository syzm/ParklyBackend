package pw.react.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pw.react.backend.models.CarPark;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CarParkRepository extends JpaRepository<CarPark, Long> {
    Optional<CarPark> findByBuildingNumberAndStreet_Id(String buildingNumber, Long id);

    @Query("SELECT cp FROM CarPark cp " +
            "WHERE (:dailyCostMin IS NULL OR cp.dailyCost > :dailyCostMin) " +
            "AND (:dailyCostMax IS NULL OR cp.dailyCost < :dailyCostMax) " +
            "AND (:iso3166Name IS NULL OR cp.street.city.country.iso3166Name LIKE %:iso3166Name%) " +
            "AND (:cityName IS NULL OR cp.street.city.name LIKE %:cityName%) " +
            "AND (:streetName IS NULL OR cp.street.name LIKE %:streetName%) " +
            "AND (:isActive IS NULL OR cp.isActive = :isActive)")
    Page<CarPark> findByFilters(@Param("dailyCostMin") Double dailyCostMin,
                                @Param("dailyCostMax") Double dailyCostMax,
                                @Param("iso3166Name") String iso3166Name,
                                @Param("cityName") String cityName,
                                @Param("streetName") String streetName,
                                @Param("isActive") Boolean isActive,
                                Pageable pageable);

    @Query("SELECT cp FROM CarPark cp " +
            "WHERE (:countryName IS NULL OR cp.street.city.country.iso3166Name = :countryName) " +
            "AND cp.isActive = true " +
            "AND EXISTS (" +
            "    SELECT 1 FROM Spot s " +
            "    WHERE s.carPark = cp " +
            "    AND NOT EXISTS (" +
            "        SELECT 1 FROM Reservation r " +
            "        WHERE r.spot = s AND r.status = 'ACTIVE' AND " +
            "        ((:startDateTime BETWEEN r.startDate AND r.endDate) OR " +
            "        (:endDateTime BETWEEN r.startDate AND r.endDate) OR " +
            "        (:startDateTime <= r.startDate AND :endDateTime >= r.endDate))" +
            "    )" +
            ") " +
            "AND (:dailyCostMin IS NULL OR cp.dailyCost >= :dailyCostMin) " +
            "AND (:dailyCostMax IS NULL OR cp.dailyCost <= :dailyCostMax)")
    Page<CarPark> findCarParksForUser(
            @Param("countryName") String countryName,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("dailyCostMin") Double dailyCostMin,
            @Param("dailyCostMax") Double dailyCostMax,
            Pageable pageable);

}