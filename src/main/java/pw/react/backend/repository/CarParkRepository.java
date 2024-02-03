package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.CarPark;

import java.util.Optional;

public interface CarParkRepository extends JpaRepository<CarPark, Long> {
    Optional<CarPark> findByBuildingNumberAndStreet_Id(String buildingNumber, Long id);
}