package io.github.tainafernandes.POCAPI.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.SiglaEstado;
import io.github.tainafernandes.POCAPI.api.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        return AddressDTO.builder().zipCode("18741-011")
                .state(SiglaEstado.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1")
                .build();
    }

    @Test
    @DisplayName("Must successfully create a address")
    public void createAddressTest() throws Exception{
        //scenery
        AddressDTO dto = createNewAddress();
        Address saveAddress = Address.builder().id(1L).zipCode("18741-011")
                .state(SiglaEstado.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1")
                .build();

        BDDMockito.given(service.save(Mockito.any(Address.class))).willReturn(saveAddress);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(ADDRESS_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("zipCode").value(dto.getZipCode()))
                .andExpect(jsonPath("state").value(dto.getState().toString()))
                .andExpect(jsonPath("city").value(dto.getCity()))
                .andExpect(jsonPath("neighborhood").value(dto.getNeighborhood()))
                .andExpect(jsonPath("street").value(dto.getStreet()))
                .andExpect(jsonPath("addressNumber").value(dto.getAddressNumber()))
                .andExpect(jsonPath("complement").value(dto.getComplement()));
    }
}
