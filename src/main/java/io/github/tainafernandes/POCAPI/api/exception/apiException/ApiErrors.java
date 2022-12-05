package io.github.tainafernandes.POCAPI.api.exception.apiException;

import io.github.tainafernandes.POCAPI.api.exception.BusinessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.validation.BindingResult;

public class ApiErrors {
    private List<String> errors; //errors que serão retornados nos testes

    public ApiErrors(BindingResult bindingResult){
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex){
        this.errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors(){ //create get errors to return in the API
        return errors;
    }
}
