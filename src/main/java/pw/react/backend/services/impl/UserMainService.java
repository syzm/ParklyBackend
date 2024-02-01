package pw.react.backend.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pw.react.backend.dto.UserDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.mapper.UserMapper;
import pw.react.backend.models.User;
import pw.react.backend.repository.UserRepository;
import pw.react.backend.services.UserService;
import pw.react.backend.utils.Utils;

import java.util.Optional;

import static pw.react.backend.mapper.UserMapper.mapToUser;
import static pw.react.backend.mapper.UserMapper.mapToUserDto;

@Service
public class UserMainService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    protected final UserRepository userRepository;
    protected final PasswordEncoder passwordEncoder;

    public UserMainService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);

        if (isValidUser(user)) {
            log.info("User is valid");
            Optional<User> dbUser = userRepository.findByEmail(user.getEmail());

            if (dbUser.isPresent()) {
                log.error("User with the same email already exists.");
                throw new UserValidationException("User with the same email already exists.");
            }

            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user = userRepository.save(user);

            return UserMapper.mapToUserDto(user);
        } else {
            log.error("User validation failed.");
            throw new UserValidationException("User validation failed.");
        }
    }

    @Override
    public UserDto getUserById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User does not exist " + id));

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User does not exist " + id));

        BeanUtils.copyProperties(updatedUser, user, Utils.getNullAndEmailPropertyNames(updatedUser));

        User updatedUserObj = userRepository.save(user);

        return UserMapper.mapToUserDto(updatedUserObj);
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
