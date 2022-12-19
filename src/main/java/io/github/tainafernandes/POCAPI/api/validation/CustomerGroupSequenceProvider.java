package io.github.tainafernandes.POCAPI.api.validation;

import io.github.tainafernandes.POCAPI.api.DTO.CustomerDTO;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

public class CustomerGroupSequenceProvider implements DefaultGroupSequenceProvider<CustomerDTO> {
    @Override
    public List<Class<?>> getValidationGroups(CustomerDTO customerDTO) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(CustomerDTO.class);

        if(isSelectedPerson(customerDTO)){ //check to avoid NullPointerException
            groups.add(customerDTO.getDocumentType().getGroup());
        }
        return groups;
    }

    protected boolean isSelectedPerson(CustomerDTO customerDTO){
        return customerDTO != null && customerDTO.getDocumentType() != null;
    }
}
