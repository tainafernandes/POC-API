package io.github.tainafernandes.POCAPI.api.DTO;

import io.github.tainafernandes.POCAPI.api.entities.Address;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.validation.CnpjGroup;
import io.github.tainafernandes.POCAPI.api.validation.CpfGroup;
import io.github.tainafernandes.POCAPI.api.validation.CustomerGroupSequenceProvider;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GroupSequenceProvider(CustomerGroupSequenceProvider.class)
public class CustomerDTO {

    private Long id;
    @NotBlank
    private String name;
    @Email(message = "Invalid email")
    @NotBlank
    private String email;
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    @NotBlank(message = "CPF/CNPJ is required")
    private String document;
    @NotNull
    @Enumerated(EnumType.STRING)
    private documentType documentType;
    @NotBlank
    private String phoneNumber;

    private List<Address> addressList;
}
