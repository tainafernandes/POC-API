package io.github.tainafernandes.POCAPI.api.services.impl;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class CustomerServiceImpl {

    private CustomerRepository repository;
    public Customer save(Customer customer){
        if(repository.existsByDocument(customer.getDocument())){
            throw new BusinessException("Document already registered");
        }
        return repository.save(customer);
    }

    public Optional<Customer> getById(Long id){
        return  this.repository.findById(id);
    }
}
