package io.github.tainafernandes.POCAPI.api.repository;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import java.util.Optional;
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

    @Test
    @DisplayName("It must return false when there is no Address in the base with the street and number informed")
    public void returnFalseWhenStreetAndAddressNumberDoesntExists(){
        //scenery
        String street = "Estrada do Pedroso";
        String addressNumber = "52";

        //execution
        boolean exist = repository.existsByStreetAndAddressNumber(street, addressNumber);

        //verification
        assertThat(exist).isFalse();

    }

    @Test
    @DisplayName("Must get a Address by Id")
    public void findByIdTest(){
        //scenery
        Address address = createAddress();
        entityManager.persist(address);

        //execution
        Optional<Address> foundAddress = repository.findById(address.getId());

        //verification
        assertThat(foundAddress.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Must save a Address")
    public void saveAddressTest(){
        //scenery
        Address address = createAddress();

        //execution
        Address savedAddress = repository.save(address);

        //verification
        assertThat(savedAddress.getId()).isNotNull();
    }

    @Test
    @DisplayName("Must delete a Address")
    public void deleteAddressTest(){
        //scenery
        Address address = createAddress();
        entityManager.persist(address);

        //execution
        Address foundAddress = entityManager.find(Address.class, address.getId());
        repository.delete(foundAddress);

        //verification
        Address deleteAddress = entityManager.find(Address.class, address.getId());
        assertThat(deleteAddress).isNull();
    }
}
