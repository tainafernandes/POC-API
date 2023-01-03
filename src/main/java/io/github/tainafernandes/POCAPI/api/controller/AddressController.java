package io.github.tainafernandes.POCAPI.api.controller;

import io.github.tainafernandes.POCAPI.api.DTO.request.AddressRequestDto;
import io.github.tainafernandes.POCAPI.api.DTO.response.AddressResponseDto;
import io.github.tainafernandes.POCAPI.api.DTO.request.AddressViaCepDTO;
import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.exception.AddressException;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.exception.apiException.ApiErrors;
import io.github.tainafernandes.POCAPI.api.service.impl.AddressServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressServiceImpl service;
    private final ModelMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "addressCreate")
    public AddressResponseDto create(@RequestBody @Valid AddressViaCepDTO dto) throws Exception {
       // AddressViaCepDTO entity = mapper.map(dto, AddressViaCepDTO.class);
        final Address save = service.save(dto);
        return mapper.map(save, AddressResponseDto.class);
    }

//    @PostMapping("/v2") //Adicionando versionamento
//    @ResponseStatus(HttpStatus.CREATED)
//    public AddressResponseDto createV2(@RequestBody @Valid AddressViaCepDTO dto) throws Exception {
//        final Address save = service.save(dto, 6);
//        return mapper.map(save, AddressResponseDto.class);
//    }

    @GetMapping("{id}")
    @Cacheable("addressGet")
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto get(@PathVariable Long id) {
        return mapper.map(service.getById(id), AddressResponseDto.class);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "addressDelete")
    public void delete(@PathVariable Long id){
        Address address = service.getById(id);
        service.delete(address);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "addressUpdate")
    public AddressResponseDto update(@PathVariable Long id, @RequestBody @Valid AddressRequestDto dto){
        return mapper.map(service.update(id, dto), AddressResponseDto.class);
    }

    @GetMapping
    @Cacheable("addressFind")
    @ResponseStatus(HttpStatus.OK)
    public Page<AddressResponseDto> find(AddressResponseDto dto, Pageable pageRequest){
        Address filter = mapper.map(dto, Address.class);
        Page<Address> result = service.find(filter, pageRequest);
        List<AddressResponseDto> list = result.getContent().stream()
                .map(entity -> mapper.map(entity, AddressResponseDto.class))
                .collect(Collectors.toList());
        return new PageImpl<AddressResponseDto>(list, pageRequest, result.getTotalElements());
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

    @ExceptionHandler(AddressException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleAddressException(AddressException ex){
        return new ApiErrors(ex);
    }
}
