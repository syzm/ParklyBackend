package pw.react.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    Optional<Image> findByCarParkId(Long carParkId);
}
