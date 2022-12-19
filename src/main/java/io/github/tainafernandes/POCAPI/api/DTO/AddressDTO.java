package io.github.tainafernandes.POCAPI.api.DTO;

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
    private Long id;
    @NotBlank
    private String state;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String street;
    @NotBlank
    private String addressNumber;
    @NotBlank
    private String complement;
    @NotBlank
    private String zipCode;
    @NotNull
    private Boolean mainAddress;

    private Long customerId;

}
