package io.github.tainafernandes.POCAPI.api.service.impl;

import io.github.tainafernandes.POCAPI.api.DTO.request.AddressRequestDto;
import io.github.tainafernandes.POCAPI.api.DTO.response.AddressResponseDto;
import io.github.tainafernandes.POCAPI.api.DTO.request.AddressViaCepDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.AddressException;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.integrations.viaCep;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository repository;

    private final ModelMapper mapper;

    private final CustomerRepository customerRepository;

    private final viaCep viaCep;


    @Override
    @Transactional
    public Address save(AddressViaCepDTO addressViaCepDTO) throws Exception {
        //Consumo da API Externa
        AddressViaCepDTO viaCepDTO = viaCep.getCepDTO(addressViaCepDTO.getCep());
        AddressResponseDto address = new AddressResponseDto();
        address.setZipCode(addressViaCepDTO.getCep());
        address.setState(viaCepDTO.getUf());
        address.setCity(viaCepDTO.getLocalidade());
        address.setAddressNumber(addressViaCepDTO.getAddressNumber());
        address.setStreet(viaCepDTO.getLogradouro());
        address.setDistrict(viaCepDTO.getBairro());
        address.setCustomerId(addressViaCepDTO.getCustomerId());
        address.setMainAddress(addressViaCepDTO.getMainAddress());
        address.setComplement(addressViaCepDTO.getComplemento());

        //Fim consumo

        Customer customer = customerRepository.findById(address.getCustomerId()).orElseThrow();
        if (repository.existsByStreetAndAddressNumber(address.getStreet(), address.getAddressNumber())) {
            throw new BusinessException("Address already registered");
        }
        if (customer.getAddresses().isEmpty()) {
            address.setMainAddress(true);
        } else {
            address.setMainAddress(false);
        }
        if (customer.getAddresses().size() > 5) {
            throw new AddressException("You have reached the maximum amount of 5 registered addresses");
        }
        return repository.save(mapper.map(address, Address.class));
    }

    @Override
    public Address getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new AddressException("Id não encontrado"));
    }

    @Override
    @Transactional
    public void delete(Address address) {
        if (address == null || address.getId() == null) {
            throw new IllegalArgumentException("Address id cant be null");
        }
        this.repository.delete(address);
    }

    @Override
    @Transactional
    public Address update(Long id, AddressRequestDto addressRequestDto) {
        if(addressRequestDto == null) {
            throw new IllegalArgumentException("Address id cant be null");
        }

        Address address = getById(id);
        address.setZipCode(addressRequestDto.getZipCode());
        address.setState(addressRequestDto.getState());
        address.setCity(addressRequestDto.getCity());
        address.setDistrict(addressRequestDto.getDistrict());
        address.setAddressNumber(addressRequestDto.getAddressNumber());
        address.setComplement(addressRequestDto.getComplement());

        if(Boolean.TRUE.equals(addressRequestDto.getMainAddress())){
            address.getCustomer().getAddresses()
                    .stream().forEach(addr -> addr.setMainAddress(false)); //garantindo que todos os outros address são false
            address.setMainAddress(true);
        } else if (Boolean.TRUE.equals(address.getMainAddress())){
            throw new AddressException("You must have at least one address as the main");
        }
        return repository.save(address);
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
