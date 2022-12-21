package io.github.tainafernandes.POCAPI.api.DTO.response;

import jakarta.persistence.Column;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private Long id;
    private String state;
    private String city;
    private String district;
    private String street;
    private String addressNumber;
    private String complement;
    private String zipCode;
    private Boolean mainAddress;
    private Long customerId;

    @Version
    @Column
    private long version;

}
