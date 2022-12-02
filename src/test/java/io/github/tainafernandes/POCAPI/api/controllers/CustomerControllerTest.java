package io.github.tainafernandes.POCAPI.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tainafernandes.POCAPI.api.DTO.CustomerDTO;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.services.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
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
    CustomerService service;
    @Test
    @DisplayName("Must successfully create a customer")
    public void createCustomerTest() throws Exception{
        //cenário
        CustomerDTO dto = CustomerDTO.builder().name("Josefa").email("josefa@email.com")
                .document("12345678900").documentType(documentType.PF).phoneNumber("11999999999")
                .build();
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
    public void createInvalidCustomerTest(){

    }
}
