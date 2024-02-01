package pw.react.backend.security.common;

import org.springframework.security.core.userdetails.*;
import pw.react.backend.repository.UserRepository;

import java.util.Optional;

public class CommonUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CommonUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<pw.react.backend.models.User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
