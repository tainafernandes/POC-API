package io.github.tainafernandes.POCAPI.api.services;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import java.util.Optional;

public interface CustomerService {
    Customer save(Customer any);

    Optional<Customer> getById(Long id);
}
