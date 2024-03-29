package pw.react.backend.services.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.repository.CustomerRepository;
import pw.react.backend.repository.UserRepository;
import pw.react.backend.services.UserService;

@Profile("!batch")
public class NonBatchConfig {

    @Bean
    public UserService userService(UserRepository userRepository, CustomerRepository customerRepository,
                                   PasswordEncoder passwordEncoder) {
        return new UserMainService(userRepository, customerRepository, passwordEncoder);
    }

}
