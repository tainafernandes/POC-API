package io.github.tainafernandes.POCAPI.api.DTO.response;

import jakarta.persistence.Column;
import jakarta.persistence.Version;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;
    private List<AddressResponseDto> addresses;
    @Version
    @Column
    private long version;

}
