package io.github.tainafernandes.POCAPI.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
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

@WebMvcTest(controllers = AddressController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdressControllerTest {

    static String ADDRESS_API = "/api/address";
    @Autowired
    MockMvc mvc; //utilizado para as requisições
    @MockBean
    AddressServiceImpl service;

    @Autowired(required = false)
    private CustomerRepository customerRepository;

    private AddressDTO createNewAddress(){
        return AddressDTO.builder().zipCode("18741-011")
                .state(StateAbbreviations.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1").mainAddress(true)
                .mainAddress(true).build();
    }
    //preciso informar o Id do customer que quero cadastrar o endereço

    @Test
    @DisplayName("Must successfully create a address")
    public void createAddressTest() throws Exception{
        //scenery
        AddressDTO dto = createNewAddress();
        Address saveAddress = Address.builder().id(1L).zipCode("18741-011")
                .state(StateAbbreviations.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1").mainAddress(true)
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
                .andExpect(jsonPath("complement").value(dto.getComplement()))
                .andExpect(jsonPath("mainAddress").value(dto.getMainAddress()));
    }

    @Test
    @DisplayName("Should throw an error when there is not all the data to create an address")
    public void createInvalidAddressTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new AddressDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(ADDRESS_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(8)));
    }
}
