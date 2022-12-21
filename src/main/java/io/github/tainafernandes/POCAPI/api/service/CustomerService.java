package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.DTO.request.CustomerRequestDto;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Customer save(CustomerRequestDto any);

    Optional<Customer> getById(Long id);

    void delete(Customer customer);

    Customer update(Customer customer);

    Page<Customer> find(Customer filter, Pageable pageRequest);

}
