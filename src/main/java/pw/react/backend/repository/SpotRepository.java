package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}
