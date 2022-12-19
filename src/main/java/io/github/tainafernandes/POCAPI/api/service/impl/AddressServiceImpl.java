package io.github.tainafernandes.POCAPI.api.service.impl;

import com.google.gson.Gson;
import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.DTO.AddressViaCepDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.AddressException;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.repository.AddressRepository;
import io.github.tainafernandes.POCAPI.api.repository.CustomerRepository;
import io.github.tainafernandes.POCAPI.api.service.AddressService;
import io.github.tainafernandes.POCAPI.api.integrations.viaCep;
import java.util.Optional;
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
    public Address save(AddressViaCepDTO addressViaCepDTO) throws Exception {
        //Consumo da API Externa
        AddressViaCepDTO viaCepDTO = viaCep.getCepDTO(addressViaCepDTO.getCep());
        AddressDTO address = new AddressDTO();
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
        if(repository.existsByStreetAndAddressNumber(address.getStreet(), address.getAddressNumber())){
            throw new BusinessException("Address already registered");
        }
        if(customer.getAddress().isEmpty()){
            address.setMainAddress(true);
        } else {
            address.setMainAddress(false);
        }
        if(customer.getAddress().size() > 5){
            throw new AddressException("You have reached the maximum amount of 5 registered addresses");
        }
        return repository.save(mapper.map(address, Address.class));
    }

    @Override
    public Optional<Address> getById(Long id) {
        if (repository.findById(id).isEmpty()){
            new AddressException("Address not found");
        }
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
