package io.github.tainafernandes.POCAPI.api.controllers;

import io.github.tainafernandes.POCAPI.api.DTO.CustomerDTO;
import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO create(@RequestBody CustomerDTO dto){
        Customer customerEntity = Customer.builder().name(dto.getName()).email(dto.getEmail())
                .document(dto.getDocument()).documentType(dto.getDocumentType())
                .phoneNumber(dto.getPhoneNumber()).build();

        customerEntity = service.save(customerEntity);

        return CustomerDTO.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getName())
                .email(customerEntity.getEmail()).document(customerEntity.getDocument())
                .documentType(customerEntity.getDocumentType())
                .phoneNumber(customerEntity.getPhoneNumber())
                .build();
    }
}
