package io.github.tainafernandes.POCAPI.api.services.impl;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.services.CustomerService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    @Override
    public Customer save(Customer customer){
        if(repository.existsByDocument(customer.getDocument())){
            throw new BusinessException("Document already registered");
        }
        return repository.save(customer);
    }

    @Override
    public Optional<Customer> getById(Long id){
        return this.repository.findById(id);
    }
    @Override
    public void delete(Customer customer){
        if(customer == null || customer.getId() == null){
            throw new IllegalArgumentException("Customer id cant be null");
        }
        this.repository.delete(customer);
    }
    @Override
    public Customer update (Customer customer){
        if(customer == null || customer.getId() == null){
            throw new IllegalArgumentException("Customer id cant be null");
        }
        return this.repository.save(customer);
    }
    @Override
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
