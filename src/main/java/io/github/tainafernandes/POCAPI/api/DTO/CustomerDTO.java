package io.github.tainafernandes.POCAPI.api.DTO;

import io.github.tainafernandes.POCAPI.api.enums.documentType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
