package io.github.tainafernandes.POCAPI.api.controller;

import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.exception.apiException.ApiErrors;
import io.github.tainafernandes.POCAPI.api.service.CustomerService;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressServiceImpl service;
    private final ModelMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO create(@RequestBody @Valid AddressDTO dto){
        Address entity = mapper.map(dto, Address.class);
        entity = service.save(entity);
        return mapper.map(entity, AddressDTO.class);
    }

    @GetMapping("{id}")
    public AddressDTO get(@PathVariable Long id){
        return service.getById(id)
                .map(address -> mapper.map(address, AddressDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }





    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) { //Exception thrown every time an object is validated and it is not valid
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){
        return new ApiErrors(ex);
    }
}
