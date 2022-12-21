package io.github.tainafernandes.POCAPI.api.DTO.request;

import io.github.tainafernandes.POCAPI.api.enums.documentType;
import io.github.tainafernandes.POCAPI.api.validation.CnpjGroup;
import io.github.tainafernandes.POCAPI.api.validation.CpfGroup;
import io.github.tainafernandes.POCAPI.api.validation.CustomerGroupSequenceProvider;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GroupSequenceProvider(CustomerGroupSequenceProvider.class)
public class CustomerRequestDto {

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

    @Version
    @Column
    private long version;
}
