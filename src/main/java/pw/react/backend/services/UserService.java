package pw.react.backend.services;

import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.dto.User.CustomerPatchDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import pw.react.backend.models.PageResponse;
import pw.react.backend.models.User;

public interface UserService {
       void createCustomer(CustomerCreationDto customerCreationDto);
       void createAdmin(AdminCreationDto adminCreationDto);
       CustomerInfoDto getCustomerByUserId(Long id) throws ResourceNotFoundException;
       void updateCustomer(Long id, CustomerPatchDto updatedCustomer);

       PageResponse<CustomerInfoDto> findCustomersByParameters(String firstName,
                                                               String lastName,
                                                               String email,
                                                               Pageable pageable);
       User getUserById(Long id);
}