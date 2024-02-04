package pw.react.backend.services.impl;

import org.springframework.stereotype.Service;
import pw.react.backend.repository.CustomerRepository;
import pw.react.backend.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public long getCustomerCount() {
        return customerRepository.count();
    }
}
