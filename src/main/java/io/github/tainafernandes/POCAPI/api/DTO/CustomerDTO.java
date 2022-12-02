package io.github.tainafernandes.POCAPI.api.DTO;

import io.github.tainafernandes.POCAPI.api.enums.documentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String document;
    private documentType documentType;
    private String phoneNumber;
}
