package io.github.tainafernandes.POCAPI.api.entities;

import io.github.tainafernandes.POCAPI.api.enums.documentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Long id;
    private String name;
    private String email;
    private String document;
    @Enumerated(EnumType.STRING)
    private documentType documentType;
    private String phoneNumber;
}
