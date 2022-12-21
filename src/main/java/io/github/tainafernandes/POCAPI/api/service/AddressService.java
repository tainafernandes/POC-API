package io.github.tainafernandes.POCAPI.api.service;

import io.github.tainafernandes.POCAPI.api.DTO.request.AddressRequestDto;
import io.github.tainafernandes.POCAPI.api.DTO.response.AddressResponseDto;
import io.github.tainafernandes.POCAPI.api.DTO.request.AddressViaCepDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    Address save(AddressViaCepDTO address) throws Exception;

    Address getById(Long id);

    void delete(Address address);

    Address update(Long id, AddressRequestDto addressRequestDto);

    Page<Address> find(Address filter, Pageable pageRequest);
}
