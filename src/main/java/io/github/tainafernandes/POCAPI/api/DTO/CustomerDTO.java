package io.github.tainafernandes.POCAPI.api.DTO;

import io.github.tainafernandes.POCAPI.api.enums.documentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private String document;
    @NotEmpty
    private documentType documentType;
    @NotEmpty
    private String phoneNumber;
}
