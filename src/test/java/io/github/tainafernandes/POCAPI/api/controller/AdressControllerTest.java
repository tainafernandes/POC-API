package io.github.tainafernandes.POCAPI.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
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
    MockMvc mvc; //used for requests
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

    @Test
    @DisplayName("It should return an error trying to register an address with street and repeated number")
    public void createAddressWithDuplicatedAddressAndAddressNumber() throws Exception{

        AddressDTO dto = createNewAddress();
        String json = new ObjectMapper().writeValueAsString(dto);
        String msgError = "There is already a registered address with the same street and number";
        BDDMockito.given(service.save(Mockito.any(Address.class)))
                .willThrow(new BusinessException(msgError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(ADDRESS_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(msgError));
    }

    @Test
    @DisplayName("Must get Address information")
    public void getAddressDetailsTest() throws Exception{
        Long id = 1L;

        Address address = Address.builder().id(id)
                .zipCode(createNewAddress().getZipCode())
                .state(createNewAddress().getState())
                .city(createNewAddress().getCity())
                .neighborhood(createNewAddress().getNeighborhood())
                .street(createNewAddress().getStreet())
                .addressNumber(createNewAddress().getAddressNumber())
                .complement(createNewAddress().getComplement())
                .mainAddress(createNewAddress().getMainAddress())
                .build();
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(address));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(ADDRESS_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("zipCode").value(createNewAddress().getZipCode()))
                .andExpect(jsonPath("state").value(createNewAddress().getState().toString()))
                .andExpect(jsonPath("city").value(createNewAddress().getCity()))
                .andExpect(jsonPath("neighborhood").value(createNewAddress().getNeighborhood()))
                .andExpect(jsonPath("street").value(createNewAddress().getStreet()))
                .andExpect(jsonPath("addressNumber").value(createNewAddress().getAddressNumber()))
                .andExpect(jsonPath("complement").value(createNewAddress().getComplement()))
                .andExpect(jsonPath("mainAddress").value(createNewAddress().getMainAddress()));
    }

    @Test
    @DisplayName("Should return resource not found when sought address does not exist")
    public void addressNotFoundTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(ADDRESS_API.concat("/"+1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
