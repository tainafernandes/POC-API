package io.github.tainafernandes.POCAPI.api.controller;

import io.github.tainafernandes.POCAPI.api.DTO.AddressDTO;
import io.github.tainafernandes.POCAPI.api.DTO.CustomerDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.exception.apiException.ApiErrors;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        //Address entity = mapper.map(dto, Address.class);
        final Address save = service.save(dto);
        return mapper.map(save, AddressDTO.class);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDTO get(@PathVariable Long id){
        return service.getById(id)
                .map(address -> mapper.map(address, AddressDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Address address = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(address);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDTO update(@PathVariable Long id, @RequestBody @Valid AddressDTO dto){
        return service.getById(id).map(address -> {
            address.setZipCode(dto.getZipCode());
            address.setState(dto.getState());
            address.setCity(dto.getCity());
            address.setNeighborhood(dto.getNeighborhood());
            address.setAddressNumber(dto.getAddressNumber());
            address.setComplement(dto.getComplement());
            address.setMainAddress(dto.getMainAddress());

            service.update(address);
            return mapper.map(address, AddressDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AddressDTO> find(AddressDTO dto, Pageable pageRequest){
        Address filter = mapper.map(dto, Address.class);
        Page<Address> result = service.find(filter, pageRequest);
        List<AddressDTO> list = result.getContent().stream()
                .map(entity -> mapper.map(entity, AddressDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<AddressDTO>(list, pageRequest, result.getTotalElements());
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
