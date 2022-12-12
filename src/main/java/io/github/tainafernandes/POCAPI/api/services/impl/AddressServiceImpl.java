package io.github.tainafernandes.POCAPI.api.services.impl;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository repository;
    @Override
    public Address save(Address address) {
        if(repository.existsByStreetAndAddressNumber(address.getStreet(), address.getAddressNumber())){
            throw new BusinessException("Address already registered");
        }
        return repository.save(address);
    }
}
