package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.City;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndCountry_Id(String name, Long country_id);
}
