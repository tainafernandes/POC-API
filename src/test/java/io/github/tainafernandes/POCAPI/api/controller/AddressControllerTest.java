package io.github.tainafernandes.POCAPI.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tainafernandes.POCAPI.api.DTO.request.AddressRequestDto;
import io.github.tainafernandes.POCAPI.api.DTO.request.AddressViaCepDTO;
import io.github.tainafernandes.POCAPI.api.DTO.response.AddressResponseDto;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.exception.AddressException;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
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

;

@WebMvcTest(controllers = AddressController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AddressControllerTest {

    static String ADDRESS_API = "/api/address";
    @Autowired
    MockMvc mvc; //used for requests
    @MockBean
    AddressServiceImpl service;
    @MockBean
    private AddressRepository addressRepository;
    @MockBean
    ModelMapper mapper;

    private AddressViaCepDTO createViaCepDTO(){
        return AddressViaCepDTO.builder().cep("09132-530")
                .logradouro("Rua Éden").addressNumber("25")
                .complemento("casa 1").bairro("Jardim Santo André")
                .localidade("Santo André").uf("SP").mainAddress(true)
                .build();
    }
    private AddressResponseDto createResponseAddressDto(){
        return AddressResponseDto.builder().id(1L).state("SP").city("Santo André")
                .district("Jardim Santo André").street("Rua Éden")
                .addressNumber("25").complement("casa 1")
                .zipCode("09132-530").mainAddress(true).customerId(1L)
                .build();
    }

    private AddressRequestDto createRequestAddressDto(){
        return AddressRequestDto.builder().state("SP").city("Jundiaí")
                .district("Jardim Estádio").street("Rua Sorocaba")
                .addressNumber("43").complement("casa 1")
                .zipCode("13203-603").mainAddress(true).customerId(1L)
                .build();
    }
    //preciso informar o Id do customer que quero cadastrar o endereço

    @Test
    @DisplayName("Must successfully create a address")
    public void createAddressTest() throws Exception{
        //scenery
        AddressViaCepDTO dto = createViaCepDTO();
        Address save = Address.builder()
                .id(1L)
                .state("SP")
                .city("Santo André")
                .district("Jardim Santo André")
                .street("Rua Éden")
                .addressNumber("25")
                .complement("casa 1")
                .zipCode("09132-530")
                .mainAddress(true)
                .build();

        //given: espero alguma informação. Ex: dado tal coisa, me retorne isso

        BDDMockito.given(service.save(Mockito.any(AddressViaCepDTO.class)))
                .willReturn(save);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(ADDRESS_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("zipCode").value(dto.getCep()))
                .andExpect(jsonPath("street").value(dto.getLogradouro()))
                .andExpect(jsonPath("addressNumber").value(dto.getAddressNumber()))
                .andExpect(jsonPath("complement").value(dto.getComplemento()))
                .andExpect(jsonPath("district").value(dto.getBairro()))
                .andExpect(jsonPath("city").value(dto.getLocalidade()))
                .andExpect(jsonPath("state").value(dto.getUf()))
                .andExpect(jsonPath("mainAddress").value(dto.getMainAddress()));
    }

    @Test
    @DisplayName("Should throw an error when there is not all the data to create an address")
    public void createInvalidAddressTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new AddressResponseDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(ADDRESS_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(2)));
    }

    @Test
    @DisplayName("An error should be returned when trying to register an address with a blank zipCode")
    public void createAddressWithBlankZipCode() throws Exception{

        AddressViaCepDTO dto = createViaCepDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        String msgError = "zipCode is a required field";
        BDDMockito.given(service.save(Mockito.any(AddressViaCepDTO.class)))
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
    @DisplayName("An error should be returned when trying to register an address with a blank addressNumber")
    public void createAddressWithBlankAddressNumber() throws Exception{

        AddressViaCepDTO dto = createViaCepDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        String msgError = "addressNumber is a required field";
        BDDMockito.given(service.save(Mockito.any(AddressViaCepDTO.class)))
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
                .zipCode(createResponseAddressDto().getZipCode())
                .state(createResponseAddressDto().getState())
                .city(createResponseAddressDto().getCity())
                .district(createResponseAddressDto().getDistrict())
                .street(createResponseAddressDto().getStreet())
                .addressNumber(createResponseAddressDto().getAddressNumber())
                .complement(createResponseAddressDto().getComplement())
                .mainAddress(createResponseAddressDto().getMainAddress())
                .build();
        BDDMockito.given(service.getById(id)).willReturn(address);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(ADDRESS_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("zipCode").value(createResponseAddressDto().getZipCode()))
                .andExpect(jsonPath("state").value(createResponseAddressDto().getState()))
                .andExpect(jsonPath("city").value(createResponseAddressDto().getCity()))
                .andExpect(jsonPath("district").value(createResponseAddressDto().getDistrict()))
                .andExpect(jsonPath("street").value(createResponseAddressDto().getStreet()))
                .andExpect(jsonPath("addressNumber").value(createResponseAddressDto().getAddressNumber()))
                .andExpect(jsonPath("complement").value(createResponseAddressDto().getComplement()))
                .andExpect(jsonPath("mainAddress").value(createResponseAddressDto().getMainAddress()));
    }

    @Test
    @DisplayName("Should return 404 when sought address does not exist")
    public void addressNotFoundTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willThrow(new AddressException("Id not found"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(ADDRESS_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Must delete a Address")
    public void deleteAddressTest() throws Exception{

        Address address = Address.builder().id(1L).build();

        BDDMockito.given(service.getById(anyLong())).willReturn(address);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(ADDRESS_API.concat("/"+1));

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return resource not found when not finding address to delete")
    public void deleteInexistentAddressTest() throws Exception{

        BDDMockito.given(service.getById(anyLong())).willThrow(new AddressException("Inexistent Address"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(ADDRESS_API.concat("/"+1));

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Must update a address")
    public void updateAddressTest() throws Exception {
        Long id = 1L;

        AddressRequestDto dto = createRequestAddressDto();
        String json = new ObjectMapper().writeValueAsString(dto);

        Address updatedAddress = Address.builder().id(id).zipCode("09132-530").state("SP")
                .city("Santo André").district("Jardim Santo André").street("Rua Éden")
                .addressNumber("25").complement("Casa 1").mainAddress(true).build();

        AddressResponseDto responseDto = createResponseAddressDto();

            BDDMockito.given(service.update(id, dto)).willReturn(updatedAddress);
            BDDMockito.given(mapper.map(updatedAddress, AddressResponseDto.class)).willReturn(responseDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(ADDRESS_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(responseDto.getId()))
                .andExpect(jsonPath("zipCode").value(responseDto.getZipCode()))
                .andExpect(jsonPath("state").value(responseDto.getState()))
                .andExpect(jsonPath("city").value(responseDto.getCity()))
                .andExpect(jsonPath("district").value(responseDto.getDistrict()))
                .andExpect(jsonPath("street").value(responseDto.getStreet()))
                .andExpect(jsonPath("addressNumber").value(responseDto.getAddressNumber()))
                .andExpect(jsonPath("complement").value(responseDto.getComplement()))
                .andExpect(jsonPath("customerId").value(responseDto.getCustomerId()))
                .andExpect(jsonPath("mainAddress").value(responseDto.getMainAddress()));

    }

//    @Test
//    @DisplayName("Should return 404 when trying to update a non-existent address")
//    public void updateInexistentAddressTest() throws Exception{
//
//        String json = new ObjectMapper().writeValueAsString(createNewAddress());
//        createRequestAddressDto().setId(3L);
//
//        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .put(ADDRESS_API.concat("/"+1))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(json);
//
//        mvc.perform(request)
//                .andExpect(status().isNotFound());
//    }

//    @Test
//    @DisplayName("Must filter address")
//    public void findAddressTest() throws Exception{
//        Long id = 1L;
//
//        Address address = Address.builder().id(id)
//                .zipCode(createNewAddress().getZipCode())
//                .state(createNewAddress().getState())
//                .city(createNewAddress().getCity())
//                .neighborhood(createNewAddress().getNeighborhood())
//                .street(createNewAddress().getStreet())
//                .addressNumber(createNewAddress().getAddressNumber())
//                .complement(createNewAddress().getComplement())
//                .mainAddress(createNewAddress().getMainAddress())
//                .build();
//
//        BDDMockito.given(service.find(Mockito.any(Address.class), Mockito.any(Pageable.class)))
//                .willReturn(new PageImpl<Address>(Arrays.asList(address), PageRequest.of(0,100), 1));
//
//        String queryString = String.format("?zipCode=%s&page=0&size=100",
//                address.getZipCode());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .get(ADDRESS_API.concat(queryString))
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("content", hasSize(1)))
//                .andExpect(jsonPath("totalElements").value(1))
//                .andExpect(jsonPath("pageable.pageSize").value(100))
//                .andExpect(jsonPath("pageable.pageNumber").value(0));
//    }
}
