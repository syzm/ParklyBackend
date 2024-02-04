package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pw.react.backend.models.User;
import pw.react.backend.models.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT c FROM Customer c JOIN User u ON u.id = c.id " +
            "WHERE (:firstName is null or c.firstName = :firstName) " +
            "AND (:lastName is null or c.lastName = :lastName) " +
            "AND (:email is null or u.email = :email)")
    Page<Customer> findCustomersByParameters(@Param("firstName") String firstName,
                                                    @Param("lastName") String lastName,
                                                    @Param("email") String email,
                                                    Pageable pageable);
}
