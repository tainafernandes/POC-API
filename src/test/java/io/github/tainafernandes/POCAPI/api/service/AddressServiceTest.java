package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.DTO.response.AddressResponseDto;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.integrations.viaCep;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AddressServiceTest {
    AddressServiceImpl service;
    @MockBean
    ModelMapper mapper;
    @MockBean
    CustomerRepository customerRepository;
    @MockBean
    AddressRepository repository;
    viaCep viaCep;

    @BeforeEach
    public void setUp(){
        this.service = new AddressServiceImpl(repository, mapper, customerRepository, viaCep);
    }


    private AddressResponseDto createAddress(){
        return AddressResponseDto.builder().zipCode("18741-011")
                .state(StateAbbreviations.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1").mainAddress(true).customerId(1L)
                .build();
    }


    @Test
    @DisplayName("Must save a Address")
    public void saveAddressTest(){
        //scenery
        Long id = 1l;

        AddressResponseDto address = createAddress();
        address.setCustomerId(id);

        Address addressClass = mapper.map(address, Address.class);

        //return false
        Mockito.when(repository.existsByStreetAndAddressNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(addressClass)).thenReturn(
                Address.builder().id(1L)
                        .zipCode("18741-011")
                        .state(StateAbbreviations.SP).city("Santo André")
                        .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                        .addressNumber("52").complement("Casa 1").mainAddress(true)
                        .build()
        );

        //execution
        Address saveAddress = service.save(mapper.map(addressClass, AddressResponseDto.class));

        //Is equal to?!
        assertThat(saveAddress.getId()).isNotNull();
        assertThat(saveAddress.getZipCode()).isEqualTo("18741-011");
        assertThat(saveAddress.getState()).isEqualTo(StateAbbreviations.SP);
        assertThat(saveAddress.getCity()).isEqualTo("Santo André");
        assertThat(saveAddress.getNeighborhood()).isEqualTo("Vila Luzita");
        assertThat(saveAddress.getStreet()).isEqualTo("Estrada do Pedroso");
        assertThat(saveAddress.getAddressNumber()).isEqualTo("52");
        assertThat(saveAddress.getComplement()).isEqualTo("Casa 1");
    }

    @Test
    @DisplayName("Business error should be thrown when trying to register an address with a duplicate street and addressNumber")
    public void shouldNotSaveAAddressWithDuplicateStreetAndAddressNumber(){
        //scenery
        AddressResponseDto address = createAddress();
        Mockito.when(repository.existsByStreetAndAddressNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        //execution
        Throwable exception = Assertions.catchThrowable(() -> service.save(address));

        //verifications
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Address already registered");

        Mockito.verify(repository, Mockito.never()).save(address);
    }

    @Test
    @DisplayName("Must get a address by Id")
    public void getByIdTest(){
        Long id = 1L;

        //scenery
        Address address = createAddress();
        address.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(address));

        //execution
        Optional<Address> foundAddress = service.getById(id);

        //verifications
        assertThat(foundAddress.isPresent()).isTrue();
        assertThat(foundAddress.get().getId()).isEqualTo(id);
        assertThat(foundAddress.get().getZipCode()).isEqualTo(address.getZipCode());
        assertThat(foundAddress.get().getState()).isEqualTo(address.getState());
        assertThat(foundAddress.get().getCity()).isEqualTo(address.getCity());
        assertThat(foundAddress.get().getNeighborhood()).isEqualTo(address.getNeighborhood());
        assertThat(foundAddress.get().getStreet()).isEqualTo(address.getStreet());
        assertThat(foundAddress.get().getAddressNumber()).isEqualTo(address.getAddressNumber());
        assertThat(foundAddress.get().getComplement()).isEqualTo(address.getComplement());
        assertThat(foundAddress.get().getMainAddress()).isEqualTo(address.getMainAddress());
    }

    @Test
    @DisplayName("Must return empty when obtaining a Address by Id when it does not exist in the database")
    public void addressNotFoundByIdTest() {
        Long id = 1L;

        //scenery
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execution
        Optional<Address> address = service.getById(id);

        //verifications
        assertThat(address.isPresent()).isFalse();
    }

    @Test
    @DisplayName("You must delete a address when the specified id exists in the database")
    public void deleteAddressTest(){
        Long id = 1L;

        //scenery
        Address address = createAddress();
        address.setId(id);

        //execution
        assertDoesNotThrow(() -> service.delete(address));

        //verifications
        Mockito.verify(repository, Mockito.times(1)).delete(address);
    }

    @Test
    @DisplayName("Should return error when trying to delete a Address that does not exist")
    public void deleteInvalidAddressTest(){
        Address address = new Address();

        assertThrows(IllegalArgumentException.class, () -> service.delete(address));

        Mockito.verify(repository, Mockito.never()).delete(address);
    }

    @Test
    @DisplayName("Should return error when trying to update a Address that does not exist")
    public void updateInvalidAddressTest(){
        Address address = new Address();

        assertThrows(IllegalArgumentException.class, () -> service.update(address));

        Mockito.verify(repository, Mockito.never()).save(address);
    }

    @Test
    @DisplayName("You must update the Address with the given id")
    public void updateAddressTest() {
        //scenary
        Long id = 1L;

        //update address
        Address updatingAddress = Address.builder().id(id).build();

        //simulation
        Address updatedCustomer = createAddress();
        updatedCustomer.setId(id);
        Mockito.when(repository.save(updatingAddress)).thenReturn(updatedCustomer);

        //execution
        Address address = service.update(updatingAddress);

        //verifications
        assertThat(address.getId()).isEqualTo(updatedCustomer.getId());
        assertThat(address.getZipCode()).isEqualTo(updatedCustomer.getZipCode());
        assertThat(address.getState()).isEqualTo(updatedCustomer.getState());
        assertThat(address.getCity()).isEqualTo(updatedCustomer.getCity());
        assertThat(address.getNeighborhood()).isEqualTo(updatedCustomer.getNeighborhood());
        assertThat(address.getStreet()).isEqualTo(updatedCustomer.getStreet());
        assertThat(address.getAddressNumber()).isEqualTo(updatedCustomer.getAddressNumber());
        assertThat(address.getComplement()).isEqualTo(updatedCustomer.getComplement());
        assertThat(address.getMainAddress()).isEqualTo(updatedCustomer.getMainAddress());
    }

    @Test
    @DisplayName("Must filter address by properties")
    public void findAddressTest(){
        //scenary
        Address address = createAddress();

        PageRequest pageRequest = PageRequest.of(0,10);

        List<Address> list = Arrays.asList(address);
        Page<Address> page = new PageImpl<>(Arrays.asList(address), PageRequest.of(0,10), 1);
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //execution
        Page<Address> result = service.find(address, pageRequest);

        //verification
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
