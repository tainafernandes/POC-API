package io.github.tainafernandes.POCAPI.api.controller;

import io.github.tainafernandes.POCAPI.api.DTO.CustomerDTO;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import io.github.tainafernandes.POCAPI.api.exception.apiException.ApiErrors;
import io.github.tainafernandes.POCAPI.api.services.impl.CustomerServiceImpl;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl service;

    private final ModelMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO create(@RequestBody @Valid CustomerDTO dto){
        Customer entity = mapper.map(dto, Customer.class);
        entity = service.save(entity);
        return mapper.map(entity, CustomerDTO.class);
    }
    @GetMapping("{id}")
    public CustomerDTO get(@PathVariable Long id){
        return service.getById(id)
                .map(customer -> mapper.map(customer, CustomerDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Customer customer = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(customer);
    }
    @PutMapping("{id}")
    public CustomerDTO update(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto){
        return  service.getById(id).map(customer -> {
            customer.setName(dto.getName());
            customer.setEmail(dto.getEmail());
            customer.setDocument(dto.getDocument());
            customer.setDocumentType(dto.getDocumentType());
            customer.setPhoneNumber(dto.getPhoneNumber());

            service.update(customer);
            return mapper.map(customer, CustomerDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    public Page<CustomerDTO> find(CustomerDTO dto, Pageable pageRequest){
        Customer filter = mapper.map(dto, Customer.class);
        Page<Customer> result = service.find(filter, pageRequest); //Here it returns a Customer page, but I have to return a CustomerDto page
        List<CustomerDTO> list = result.getContent().stream()
                .map(entity -> mapper.map(entity, CustomerDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<CustomerDTO>(list, pageRequest, result.getTotalElements());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex){ //Exception thrown every time an object is validated and it is not valid
        BindingResult bindingResult = ex.getBindingResult();
        //BindigResult - validation result that occurred when validating the obj annotated with @Valid
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){
        return new ApiErrors(ex);
    }
}
