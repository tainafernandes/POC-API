package io.github.tainafernandes.POCAPI.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long Id;
    private String zipCode;
    private String city;
    private String state;
    private String neighborhood;
    private String street;
    private String addressNumber;
    private String complement;
}
