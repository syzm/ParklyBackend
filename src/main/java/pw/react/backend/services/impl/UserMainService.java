package pw.react.backend.services.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.CustomerCreationDto;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.Customer;
import pw.react.backend.models.User;
import pw.react.backend.models.UserRole;
import pw.react.backend.repository.CustomerRepository;
import pw.react.backend.repository.UserRepository;
import pw.react.backend.services.UserService;

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
        User user = new User();
        user.setEmail(customerCreationDto.getEmail());
        user.setPassword(customerCreationDto.getPassword());
        user.setRole(UserRole.Customer);

        if (isValidUser(user)) {
            log.info("User is valid");
            Optional<User> dbUser = userRepository.findByEmail(user.getEmail());

            if (dbUser.isPresent()) {
                log.error("User with the same email already exists.");
                throw new UserValidationException("User with the same email already exists.");
            }

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
//
//    @Override
//    public CustomerDto getUserById(Long id) throws ResourceNotFoundException {
//        User user = userRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User does not exist " + id));
//
//        return UserMapper.mapToCustomerDto(user);
//    }
//
//    public void updateUser(Long id, CustomerPatchDto updatedUserDto) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User does not exist " + id));
//
//        if (user != null) {
//            BeanUtils.copyProperties(updatedUserDto, user, Utils.getNullPropertyNames(updatedUserDto));
//            userRepository.save(user);
//        } else {
//        }
//    }



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
