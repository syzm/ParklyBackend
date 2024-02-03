package pw.react.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Spot;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Page<Spot> findByCarParkId(Long carParkId, Pageable pageable);
}
