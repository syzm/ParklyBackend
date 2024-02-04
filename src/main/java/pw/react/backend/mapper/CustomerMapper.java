package pw.react.backend.mapper;


import pw.react.backend.dto.User.CustomerInfoDto;
import pw.react.backend.models.Customer;
import pw.react.backend.models.User;

public class CustomerMapper {
    public static CustomerInfoDto mapToDto(Customer customer) {
        User user = customer.getUser();

        return new CustomerInfoDto(
                customer.getUser().getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getBirthDate(),
                user.getEmail()
        );
    }
}
