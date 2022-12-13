package io.github.tainafernandes.POCAPI.api.service.impl;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.service.AddressService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Optional<Address> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Address address) {
        if(address == null || address.getId() == null){
            throw new IllegalArgumentException("Address id cant be null");
        }
        this.repository.delete(address);
    }

    @Override
    public Address update(Address address) {
        if(address == null || address.getId() == null){
            throw new IllegalArgumentException("Address id cant be null");
        }
        return this.repository.save(address);
    }

    @Override
    public Page<Address> find(Address filter, Pageable pageRequest) {
        Example<Address> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example, pageRequest);
    }

}