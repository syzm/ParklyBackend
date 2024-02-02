package pw.react.backend.services;

import pw.react.backend.dto.CustomerCreationDto;

public interface UserService {
       void createCustomer(CustomerCreationDto customerCreationDto);
//    CustomerDto getUserById(Long id) throws ResourceNotFoundException;
//    void updateUser(Long id, CustomerPatchDto updatedUser);
}