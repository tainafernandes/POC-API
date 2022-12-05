package io.github.tainafernandes.POCAPI.api.repository;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByDocument(String document);
}
