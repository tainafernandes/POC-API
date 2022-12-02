package io.github.tainafernandes.POCAPI.api.entities;

import io.github.tainafernandes.POCAPI.api.enums.documentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class Customer {

    private Long id;
    private String name;
    private String email;
    private String document;
    private documentType documentType;

    private String phoneNumber;
}
