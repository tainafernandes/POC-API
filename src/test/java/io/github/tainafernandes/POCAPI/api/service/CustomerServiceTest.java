package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.services.impl.CustomerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

    private Customer createCustomer(){
        return Customer.builder().name("Josefa")
                .email("josefa@email.com").document("12345678900")
                .documentType(documentType.PF).phoneNumber("11999999999")
                .build();
    }

    @Test
    @DisplayName("Must save a customer")
    public void saveCustomerTest(){
        //scenery
        Customer customer = createCustomer();
        //guaranteed to return false
        Mockito.when(repository.existsByDocument(Mockito.anyString())).thenReturn(false);

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

    @Test
    @DisplayName("Business error should be thrown when trying to register a Customer with a duplicate document")
    public void shouldNotSavedACustomerWithDuplicatedDocument(){
        //scenery
        Customer customer = createCustomer();
        Mockito.when(repository.existsByDocument(Mockito.anyString())).thenReturn(true);
        //returns true when the repository runs this test

        //execution - exception when trying to save a customer
        Throwable exception = Assertions.catchThrowable(() -> service.save(customer));

        //verifications
        assertThat(exception)
                .isInstanceOf(BusinessException.class)//check that this instance is of this exception
                .hasMessage("Document already registered"); //and the expected message is returned

        //if there is an error, I cannot call the save. Then let's test that it is not saving
        Mockito.verify(repository, Mockito.never()).save(customer); //It will never execute this method with this parameter
    }
}
