package io.github.tainafernandes.POCAPI.api.repository;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class AddressRepositoryTest { //Integration
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    AddressRepository repository;

    private Address createAddress(){
        return Address.builder().zipCode("18741-011")
                .state(StateAbbreviations.SP).city("Santo André")
                .neighborhood("Vila Luzita").street("Estrada do Pedroso")
                .addressNumber("52").complement("Casa 1")
                .build();
    }
    @Test
    @DisplayName("It must return true if there is already an address with the street and number registered")
    public void returnTrueWhenStreetAndAdressNumberExist(){
        //scenery
        String street = "Estrada do Pedroso";
        String number = "52";
        Address address = createAddress();
        entityManager.persist(address);

        //execution
        boolean exists = repository.existsByStreetAndAddressNumber(street, number);

        //verification
        assertThat(exists).isTrue();
    }
}
