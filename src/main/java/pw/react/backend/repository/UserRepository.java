package pw.react.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pw.react.backend.models.User;
import pw.react.backend.models.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT c FROM Customer c " +
            "JOIN c.user u " +
            "WHERE (:firstName is null OR c.firstName LIKE %:firstName%) " +
            "AND (:lastName is null OR c.lastName LIKE %:lastName%) " +
            "AND (:email is null OR u.email LIKE %:email%)")
    Page<Customer> findCustomersByParameters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            Pageable pageable);
}
