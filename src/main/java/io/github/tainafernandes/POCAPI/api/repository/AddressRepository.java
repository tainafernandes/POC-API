package io.github.tainafernandes.POCAPI.api.repository;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existByStreetAndAddressNumber(String street, String addressNumber);

}
