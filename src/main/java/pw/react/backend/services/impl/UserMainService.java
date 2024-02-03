package pw.react.backend.services.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.dto.User.CustomerPatchDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.Customer;
import pw.react.backend.models.User;
import pw.react.backend.models.UserRole;
import pw.react.backend.repository.CustomerRepository;
import pw.react.backend.repository.UserRepository;
import pw.react.backend.services.UserService;
import pw.react.backend.utils.Utils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserMainService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    protected final UserRepository userRepository;

    protected final CustomerRepository customerRepository;
    protected final PasswordEncoder passwordEncoder;


    @Override
    public void createCustomer(CustomerCreationDto customerCreationDto) {
        CheckDuplicateEmail(customerCreationDto.getEmail());

        User user = new User();
        user.setEmail(customerCreationDto.getEmail());
        user.setPassword(customerCreationDto.getPassword());
        user.setRole(UserRole.ADMIN);

        if (isValidUser(user)) {
            log.info("User is valid");

            user.setPassword(passwordEncoder.encode(customerCreationDto.getPassword()));
            user = userRepository.save(user);

            Customer customer = new Customer();
            customer.setUser(user);
            customer.setFirstName(customerCreationDto.getFirstName());
            customer.setLastName(customerCreationDto.getLastName());
            customer.setBirthDate(customerCreationDto.getBirthDate());

            customerRepository.save(customer);
        } else {
            log.error("User validation failed.");
            throw new UserValidationException("User validation failed.");
        }
    }

    @Override
    public void createAdmin(AdminCreationDto adminCreationDto) {
        CheckDuplicateEmail(adminCreationDto.getEmail());
        User user = new User();
        user.setEmail(adminCreationDto.getEmail());
        user.setPassword(adminCreationDto.getPassword());
        user.setRole(UserRole.CUSTOMER);

        if (isValidUser(user)) {
            log.info("User is valid");

            user.setPassword(passwordEncoder.encode(adminCreationDto.getPassword()));
            userRepository.save(user);
        } else {
            log.error("User validation failed.");
            throw new UserValidationException("User validation failed.");
        }
    }

    private void CheckDuplicateEmail(String adminCreationDto) {
        Optional<User> dbUser = userRepository.findByEmail(adminCreationDto);
        if (dbUser.isPresent()) {
            log.error("User with the same email already exists.");
            throw new UserValidationException("User with the same email already exists.");
        }
    }

    @Override
    public CustomerInfoDto getCustomerByUserId(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User does not exist " + id));

        Customer customer = customerRepository.findByUserId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer does not exist for user ID " + id));

        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        customerInfoDto.setFirstName(customer.getFirstName());
        customerInfoDto.setLastName(customer.getLastName());
        customerInfoDto.setEmail(user.getEmail());
        customerInfoDto.setBirthDate(customer.getBirthDate());

        return customerInfoDto;
    }

    public void updateCustomer(Long id, CustomerPatchDto updatedCustomerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User does not exist " + id));
        BeanUtils.copyProperties(updatedCustomerDto, customer, Utils.getNullPropertyNames(updatedCustomerDto));
        customerRepository.save(customer);
    }

    private boolean isValidUser(User user) {
        if (user != null) {
            if (isValid(user.getUsername())) {
                log.error("Empty email.");
                throw new UserValidationException("Empty email.");
            }
            if (isValid(user.getPassword())) {
                log.error("Empty user password.");
                throw new UserValidationException("Empty user password.");
            }
            return true;
        }
        log.error("User is null.");
        throw new UserValidationException("User is null.");
    }
    private boolean isValid(String value) {
        return value == null || value.isBlank();
    }


}
