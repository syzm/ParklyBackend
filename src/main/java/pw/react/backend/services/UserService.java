package pw.react.backend.services;

import pw.react.backend.dto.CustomerCreationDto;
import pw.react.backend.dto.CustomerInfoDto;
import pw.react.backend.dto.CustomerPatchDto;
import pw.react.backend.exceptions.ResourceNotFoundException;

public interface UserService {
       void createCustomer(CustomerCreationDto customerCreationDto);
       CustomerInfoDto getCustomerByUserId(Long id) throws ResourceNotFoundException;
       void updateCustomer(Long id, CustomerPatchDto updatedCustomer);
}