package io.github.tainafernandes.POCAPI.api.service.impl;

import io.github.tainafernandes.POCAPI.api.DTO.request.CustomerRequestDto;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.service.CustomerService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    private final ModelMapper mapper;

    @Override
    @Transactional
    public Customer save(CustomerRequestDto customerRequestDto){
        if(repository.existsByDocument(customerRequestDto.getDocument())){
            throw new BusinessException("Document already registered");
        }
        return repository.save(mapper.map(customerRequestDto, Customer.class));
    }

    @Override
    public Optional<Customer> getById(Long id){
        return this.repository.findById(id);
    }
    @Override
    @Transactional
    public void delete(Customer customer){
        if(customer == null || customer.getId() == null){
            throw new IllegalArgumentException("Customer id cant be null");
        }
        this.repository.delete(customer);
    }
    @Override
    @Transactional
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

//    @Override
//    public Optional<Customer> getCustomerById(long id) {
//        return null;
//    }
}
