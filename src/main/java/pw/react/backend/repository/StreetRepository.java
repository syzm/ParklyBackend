package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Street;

import java.util.Optional;

public interface StreetRepository extends JpaRepository<Street, Long> {
    Optional<Street> findByNameAndCity_Id(String name, Long id);
}
