package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(Long userId);
}
