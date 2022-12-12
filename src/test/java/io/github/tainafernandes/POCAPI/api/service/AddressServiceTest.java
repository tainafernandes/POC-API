package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AddressServiceTest {
    AddressServiceImpl service;
    @MockBean
    AddressRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new AddressServiceImpl(repository);
    }

    private Address createAddress(){
        return Address.builder().zipCode("18741-011")
                .state(StateAbbreviations.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1")
                .build();
    }

    @Test
    @DisplayName("Must save a Address")
    public void saveAddressTest(){
        //scenery
        Address address = createAddress();
        //return false
        Mockito.when(repository.existsByStreetAndAddressNumber(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(address)).thenReturn(
                Address.builder().id(1L)
                        .zipCode("18741-011")
                        .state(StateAbbreviations.SP).city("Santo André")
                        .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                        .addressNumber("52").complement("Casa 1")
                        .build()
        );

        //execution
        Address saveAddress = service.save(address);

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
}
