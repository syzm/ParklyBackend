package pw.react.backend.services;

import pw.react.backend.dto.User.AdminCreationDto;
import pw.react.backend.dto.User.CustomerCreationDto;
import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.dto.User.CustomerPatchDto;
import pw.react.backend.exceptions.ResourceNotFoundException;

public interface UserService {
       void createCustomer(CustomerCreationDto customerCreationDto);
       void createAdmin(AdminCreationDto adminCreationDto);
       CustomerInfoDto getCustomerByUserId(Long id) throws ResourceNotFoundException;
       void updateCustomer(Long id, CustomerPatchDto updatedCustomer);
}