package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import java.util.Optional;

public interface AddressService {
    Address save(Address address);

    Optional<Address> getById(Long id);

    void delete(Address address);

    Address update(Address address);
}
