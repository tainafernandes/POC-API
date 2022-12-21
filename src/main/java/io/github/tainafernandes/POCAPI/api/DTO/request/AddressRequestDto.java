package io.github.tainafernandes.POCAPI.api.DTO.request;

import jakarta.persistence.Column;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {
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
    private Boolean mainAddress;

    private Long customerId;

    @Version
    @Column
    private long version;
}
