package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CustomerServiceTest { //Only unit tests

    CustomerServiceImpl service;
    @MockBean
    CustomerRepository repository;

    @BeforeEach //annotation causes method to be executed before each test
    public void setUp(){
        this.service = new CustomerServiceImpl(repository);
    }

    @Test
    @DisplayName("Must save a customer")
    public void saveCustomerTest(){
        //scenery
        Customer customer = Customer.builder().name("Josefa")
                .email("josefa@email.com").document("12345678900")
                .documentType(documentType.PF).phoneNumber("11999999999")
                .build();

        Mockito.when(repository.save(customer)).thenReturn(
                Customer.builder().id(1L)
                .name("Josefa")
                .email("josefa@email.com").document("12345678900")
                .documentType(documentType.PF).phoneNumber("11999999999")
                .build());

        //execution
        Customer savedCustomer = service.save(customer);

        //Checking if what I'm getting is equal to
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("Josefa");
        assertThat(savedCustomer.getEmail()).isEqualTo("josefa@email.com");
        assertThat(savedCustomer.getDocument()).isEqualTo("12345678900");
        assertThat(savedCustomer.getDocumentType()).isEqualTo(documentType.PF);
        assertThat(savedCustomer.getPhoneNumber()).isEqualTo("11999999999");
    }
}
