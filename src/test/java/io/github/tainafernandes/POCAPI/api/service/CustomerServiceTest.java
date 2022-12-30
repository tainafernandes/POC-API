package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.DTO.request.CustomerRequestDto;
import io.github.tainafernandes.POCAPI.api.DTO.response.CustomerResponseDto;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.service.impl.CustomerServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CustomerServiceTest { //Only unit tests

    CustomerServiceImpl service;
    @MockBean
    CustomerRepository repository;
    @MockBean
    ModelMapper mapper;

    @BeforeEach //annotation causes method to be executed before each test
    public void setUp(){
        this.service = new CustomerServiceImpl(repository, mapper);
    }

    private CustomerRequestDto createCustomerRequestDto(){
        return CustomerRequestDto.builder().name("Josefa")
                .email("josefa@email.com").document("12345678900")
                .documentType(documentType.PF).phoneNumber("11999999999")
                .build();
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
        CustomerRequestDto customerRequestDto = createCustomerRequestDto();
        //guaranteed to return false
        Mockito.when(repository.existsByDocument(Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(mapper.map(customerRequestDto, Customer.class))).thenReturn(
                Customer.builder().id(1L)
                .name("Josefa")
                .email("josefa@email.com").document("12345678900")
                .documentType(documentType.PF).phoneNumber("11999999999")
                .build());

        //execution
        Customer savedCustomer = service.save(customerRequestDto);

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
        CustomerRequestDto customerRequestDto = createCustomerRequestDto();
        Mockito.when(repository.existsByDocument(Mockito.anyString())).thenReturn(true);
        //returns true when the repository runs this test

        //execution - exception when trying to save a customer
        Throwable exception = Assertions.catchThrowable(() -> service.save(customerRequestDto));

        //verifications
        assertThat(exception)
                .isInstanceOf(BusinessException.class)//check that this instance is of this exception
                .hasMessage("Document already registered"); //and the expected message is returned

        //if there is an error, I cannot call the save. Then let's test that it is not saving
        Mockito.verify(repository, Mockito.never()).save(mapper.map(customerRequestDto, Customer.class)); //It will never execute this method with this parameter
    }

    @Test
    @DisplayName("Must get a customer by Id")
    public void getByIdTest(){
        Long id = 1l;

        Customer customer = createCustomer();
        customer.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(customer));

        //execution
        Optional<Customer> foundCustomer = service.getById(id);

        //verifications
        assertThat(foundCustomer.isPresent()).isTrue();
        assertThat(foundCustomer.get().getId()).isEqualTo(id);
        assertThat(foundCustomer.get().getName()).isEqualTo(customer.getName());
        assertThat(foundCustomer.get().getEmail()).isEqualTo(customer.getEmail());
        assertThat(foundCustomer.get().getDocument()).isEqualTo(customer.getDocument());
        assertThat(foundCustomer.get().getDocumentType()).isEqualTo(customer.getDocumentType());
        assertThat(foundCustomer.get().getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
    }

    @Test
    @DisplayName("Must return empty when obtaining a customer by Id when it does not exist in the database")
    public void customerNotFoundByIdTest(){
        Long id = 1l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execution
        Optional<Customer> customer = service.getById(id);

        //verifications
        assertThat(customer.isPresent()).isFalse();
    }

    @Test
    @DisplayName("You must delete a customer when the specified id exists in the database")
    public void deleteCustomerTest(){
        Long id = 1l;

        Customer customer = createCustomer();
        customer.setId(id);

        // execution
        assertDoesNotThrow(() -> service.delete(customer));

        //verifications
        Mockito.verify(repository, Mockito.times(1)).delete(customer);

        //DESSA MANEIRA O TESTE TBM PASSA.
//        Customer customer = createCustomer();
//        customer.setId(id);
//        Mockito.when(repository.findById(id)).thenReturn(Optional.of(customer));
//
//        Optional<Customer> foundCustomer = service.getById(id);
//
//        assertThat(foundCustomer.isPresent()).isTrue();
//        service.delete(customer);
    }

    @Test
    @DisplayName("Should return error when trying to delete a Customer that does not exist")
    public void deleteInvalidCustomerTest(){
        Customer customer = new Customer();

        assertThrows(IllegalArgumentException.class, () -> service.delete(customer));

        Mockito.verify(repository, Mockito.never()).delete(customer); //Never call method delete
    }

    @Test
    @DisplayName("Should return error when trying to update a Customer that does not exist")
    public void updateInvalidCustomerTest(){
        Customer customer = new Customer();

        assertThrows(IllegalArgumentException.class, () -> service.update(customer));

        Mockito.verify(repository, Mockito.never()).save(customer); //Never call method delete
    }

    @Test
    @DisplayName("You must update the customer with the given id")
    public void updateCustomerTest(){
        //scenary
        Long id = 1l;

        //customer update
        Customer updatingCustomer = Customer.builder().id(id).build();

        //simulation
        Customer updatedCustomer = createCustomer();
        updatedCustomer.setId(id);
        Mockito.when(repository.save(updatingCustomer)).thenReturn(updatedCustomer);

        //execution
        Customer customer = service.update(updatingCustomer);

        //verification
        assertThat(customer.getId()).isEqualTo(updatedCustomer.getId());
        assertThat(customer.getName()).isEqualTo(updatedCustomer.getName());
        assertThat(customer.getEmail()).isEqualTo(updatedCustomer.getEmail());
        assertThat(customer.getDocument()).isEqualTo(updatedCustomer.getDocument());
        assertThat(customer.getDocumentType()).isEqualTo(updatedCustomer.getDocumentType());
        assertThat(customer.getPhoneNumber()).isEqualTo(updatedCustomer.getPhoneNumber());
    }

    @Test
    @DisplayName("Must filter customers by properties")
    public void findCustomerTeste(){
        //scenary
        Customer customer = createCustomer();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Customer> list = Arrays.asList(customer);
        Page<Customer> page = new PageImpl<Customer>(Arrays.asList(customer), PageRequest.of(0, 10), 1);
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);
        //execution
        Page<Customer> result = service.find(customer, pageRequest);
        //verification
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
