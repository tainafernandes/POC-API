package io.github.tainafernandes.POCAPI.api.validation;

import io.github.tainafernandes.POCAPI.api.DTO.request.CustomerRequestDto;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

public class CustomerGroupSequenceProvider implements DefaultGroupSequenceProvider<CustomerRequestDto> {
    @Override
    public List<Class<?>> getValidationGroups(CustomerRequestDto customerRequestDto) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(CustomerRequestDto.class);

        if(isSelectedPerson(customerRequestDto)){ //check to avoid NullPointerException
            groups.add(customerRequestDto.getDocumentType().getGroup());
        }
        return groups;
    }

    protected boolean isSelectedPerson(CustomerRequestDto customerRequestDto){
        return customerRequestDto != null && customerRequestDto.getDocumentType() != null;
    }
}
