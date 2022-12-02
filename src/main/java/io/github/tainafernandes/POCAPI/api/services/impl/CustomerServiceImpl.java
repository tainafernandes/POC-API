package io.github.tainafernandes.POCAPI.api.services.impl;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class CustomerServiceImpl {

    private CustomerRepository repository;
    public Customer save(Customer customer){
        return repository.save(customer);
    }
}
