package pw.react.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pw.react.backend.models.Image;;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name); 
    
    Optional<Image> findByCarParkId(Long carParkId);
}
