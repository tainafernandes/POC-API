package io.github.tainafernandes.POCAPI.api.DTO;

import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @NotBlank
    private String zipCode;
    @NotBlank
    private String city;
    @NotNull
    @Enumerated(EnumType.STRING)
    private StateAbbreviations state;
    @NotBlank
    private String neighborhood;
    @NotBlank
    private String street;
    @NotBlank
    private String addressNumber;
    @NotBlank
    private String complement;
    @NotNull
    private Boolean mainAddress;
    private Long customerId;

}
