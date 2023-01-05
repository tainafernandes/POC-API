package io.github.tainafernandes.POCAPI.api.DTO.request;

import jakarta.persistence.Column;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {
    private Long id;
    @NotBlank(message = "State cannot be blank")
    private String state;
    @NotBlank(message = "City cannot be blank")
    private String city;
    @NotBlank(message = "District cannot be blank")
    private String district;
    @NotBlank(message = "Street cannot be blank")
    private String street;
    @NotBlank(message = "AddressNumber cannot be blank")
    private String addressNumber;
    private String complement;
    @NotBlank(message = "Zipcode cannot be blank")
    private String zipCode;
    private Boolean mainAddress;
    private Long customerId;

    @Version
    @Column
    private long version;
}
