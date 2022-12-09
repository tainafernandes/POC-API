package io.github.tainafernandes.POCAPI.api.controllers;

import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdressControllerTest {

    static String ADDRESS_API = "/address";

    @Autowired
    MockMvc mvc;

    @MockBean
    AddressServiceImpl service;

    private AddressDTO createNewAddress(){
        return AddressDTO.builder().state().build();
    }

    @Test
    @DisplayName("Must successfully create a address")
    public void createAddressTest(){

    }
}
