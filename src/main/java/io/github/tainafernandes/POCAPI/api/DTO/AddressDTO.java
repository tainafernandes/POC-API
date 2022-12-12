package io.github.tainafernandes.POCAPI.api.DTO;

import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Long id;
    private String zipCode;
    private String city;
    @NotNull
    @Enumerated(EnumType.STRING)
    private StateAbbreviations state;
    private String neighborhood;
    private String street;
    private String addressNumber;
    private String complement;
}
