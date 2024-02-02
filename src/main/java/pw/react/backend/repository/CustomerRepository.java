package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
