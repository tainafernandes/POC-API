package io.github.tainafernandes.POCAPI.api.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tainafernandes.POCAPI.api.DTO.CustomerDTO;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.services.CustomerService;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.services.impl.CustomerServiceImpl;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") //Annotations used to configure tests in RestApis
@AutoConfigureMockMvc
public class CustomerControllerTest {

    static String CUSTOMER_API = "/customers";
    @Autowired
    MockMvc mvc;
    @MockBean //specialized to create mocked instance
    CustomerServiceImpl service;

    private CustomerDTO createNewCustomer(){
        return CustomerDTO.builder().name("Josefa").email("josefa@email.com")
                .document("12345678900").documentType(documentType.PF).phoneNumber("11999999999")
                .build();
    }
    @Test
    @DisplayName("Must successfully create a customer")
    public void createCustomerTest() throws Exception{
        //cenário
        CustomerDTO dto = createNewCustomer();
        Customer savedCustomer = Customer.builder().id(1L).name("Josefa").email("josefa@email.com")
                .document("12345678900").documentType(documentType.PF).phoneNumber("11999999999")
                .build();

        BDDMockito.given(service.save(Mockito.any(Customer.class))).willReturn(savedCustomer);
        String json = new ObjectMapper().writeValueAsString(dto); //Method receives obj and transforms it into JSON

        //To create a req - Definition of the request
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(MediaType.APPLICATION_JSON) //request Json
                .accept(MediaType.APPLICATION_JSON) //response JSON
                .content(json); //JSON Object

        //making Request
        mvc
                .perform(request)
                .andExpect(status().isCreated()) //When I have a request, I will receive a response with status 201
                .andExpect(jsonPath("id").value(1L)) //return id.
                .andExpect(jsonPath("name").value(dto.getName())) //return teste
                .andExpect(jsonPath("email").value(dto.getEmail()))
                .andExpect(jsonPath("document").value(dto.getDocument()))
                .andExpect(jsonPath("documentType").value(dto.getDocumentType().toString()))
                .andExpect(jsonPath("phoneNumber").value(dto.getPhoneNumber()));
    }

    @Test
    @DisplayName("It should throw an error when there is not all the data to create a customer")
    public void createInvalidCustomerTest() throws Exception { //integrity validation

        String json = new ObjectMapper().writeValueAsString(new CustomerDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(MediaType.APPLICATION_JSON) //request Json
                .accept(MediaType.APPLICATION_JSON) //response JSON
                .content(json); //JSON Object - Null

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));//I will launch 5 errors for the 5 required fields
    }

    @Test
    @DisplayName("There should be an error when registering a document that already exists")
    public void createCustomerWithDuplicatedDocument() throws Exception{ //validação Regra de Negócio

        CustomerDTO dto = createNewCustomer();
        String json = new ObjectMapper().writeValueAsString(dto);
        String msgError = "Document already registered";
        BDDMockito.given(service.save(Mockito.any(Customer.class)))
                .willThrow(new BusinessException(msgError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CUSTOMER_API)
                .contentType(MediaType.APPLICATION_JSON) //request Json
                .accept(MediaType.APPLICATION_JSON) //response JSON
                .content(json); //JSON Object

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(msgError));
    }
    @Test
    @DisplayName("Must get customer information")
    public void getCustomerDetailsTest() throws Exception{
        //scenery (given)
        Long id = 1l;

        Customer customer = Customer.builder().id(id)
                .name(createNewCustomer().getName())
                .email(createNewCustomer().getEmail())
                .document(createNewCustomer().getDocument())
                .documentType(createNewCustomer().getDocumentType())
                .phoneNumber(createNewCustomer().getPhoneNumber())
                .build();
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(customer));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id)) //return id.
                .andExpect(jsonPath("name").value(createNewCustomer().getName())) //return teste
                .andExpect(jsonPath("email").value(createNewCustomer().getEmail()))
                .andExpect(jsonPath("document").value(createNewCustomer().getDocument()))
                .andExpect(jsonPath("documentType").value(createNewCustomer().getDocumentType().toString()))
                .andExpect(jsonPath("phoneNumber").value(createNewCustomer().getPhoneNumber()));
    }
    @Test
    @DisplayName("Should return resource not found when the customer sought does not exist")
    public void customerNotFoundTest() throws Exception{

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty()); //returns empty because it means that I didn't find a customer in the DB

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_API.concat("/"+1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Must delete a customer")
    public void deleteCustomerTest() throws Exception {
        //I don't need to pass all the information, just the id is enough to delete
        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Customer.builder().id(1l).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_API.concat("/"+1));

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return resource not found when not finding customer to delete")
    public void deleteInexistentCustomerTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_API.concat("/"+1));

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Must update a customer")
    public void updateCustomerTest() throws Exception{

        Long id = 1l;
        String json = new ObjectMapper().writeValueAsString(createNewCustomer());

        Customer updatingCustomer = Customer.builder().id(1l).name("Josefina").email("josefina@email.com")
                .document("12345678900").documentType(documentType.PF).phoneNumber("11999999999")
                .build();
        BDDMockito.given(service.getById(id))
                .willReturn(Optional.of(updatingCustomer));

        Customer updatedCustomer = Customer.builder().id(id).name("Josefa").email("josefa@email.com").document("12345678900").documentType(documentType.PF).phoneNumber("11999999999")
                .build();
        BDDMockito.given(service.update(updatingCustomer)).willReturn(updatedCustomer);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CUSTOMER_API.concat("/"+1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id)) //return id.
                .andExpect(jsonPath("name").value(createNewCustomer().getName())) //return teste
                .andExpect(jsonPath("email").value(createNewCustomer().getEmail()))
                .andExpect(jsonPath("document").value(createNewCustomer().getDocument()))
                .andExpect(jsonPath("documentType").value(createNewCustomer().getDocumentType().toString()))
                .andExpect(jsonPath("phoneNumber").value(createNewCustomer().getPhoneNumber()));;
    }

    @Test
    @DisplayName("Should return 404 when trying to update a non-existent customer")
    public void updateInexistentCustomerTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(createNewCustomer());

        BDDMockito.given(service.getById(Mockito.anyLong()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CUSTOMER_API.concat("/"+1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Must filter customers")
    public void findCustomersTest() throws Exception{
        Long id = 1l;

        Customer customer = Customer.builder().id(id)
                .name(createNewCustomer().getName())
                .email(createNewCustomer().getEmail())
                .document(createNewCustomer().getDocument())
                .documentType(createNewCustomer().getDocumentType())
                .phoneNumber(createNewCustomer().getPhoneNumber())
                .build();

        BDDMockito.given(service.find(Mockito.any(Customer.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Customer>(Arrays.asList(customer), PageRequest.of(0, 100), 1));
        //Pageable is used to say the page number and how many records it should bring
;
        String queryString = String.format("?name=%s&email=%s&page=0&size=100",
                customer.getName(),
                customer.getEmail());
//              customer.getDocument(), customer.getDocumentType(),customer.getPhoneNumber()) - sem parametrização (por enquanto)
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .get(CUSTOMER_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
                //content - registers
    }
}
