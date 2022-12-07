package io.github.tainafernandes.POCAPI.api.services.impl;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public void delete(Customer customer){
        if(customer == null || customer.getId() == null){
            throw new IllegalArgumentException("Customer id cant be null");
        }
        this.repository.delete(customer);
    }

    public Customer update (Customer customer){
        if(customer == null || customer.getId() == null){
            throw new IllegalArgumentException("Customer id cant be null");
        }
        return this.repository.save(customer);
    }

    public Page<Customer> find (Customer filter, Pageable pageRequest){
        Example<Customer> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)); //Containing - the value I pass, just have a piece of the word that it brings to me
        return repository.findAll(example, pageRequest);
    }
}
