package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByIso3166Name(String countryIso3166Name);
}
