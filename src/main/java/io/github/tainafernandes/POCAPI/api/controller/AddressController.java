package io.github.tainafernandes.POCAPI.api.controller;

import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.services.impl.AddressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressServiceImpl service;
    private final ModelMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO create(@RequestBody AddressDTO dto){
        Address entity = mapper.map(dto, Address.class);
        entity = service.save(entity);
        return mapper.map(entity, AddressDTO.class);
    }
}
