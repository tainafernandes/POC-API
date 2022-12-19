package io.github.tainafernandes.POCAPI.api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressViaCepDTO {

    @NotBlank(message = "zipCode is a required field")
    private String cep;
    private String logradouro;
    @NotBlank(message = "addressNumber is a required field")
    private String addressNumber;
    private String complemento;
    private String bairro;

    private String localidade;

    private String uf;

    private Boolean mainAddress;
    private Long customerId;
}
