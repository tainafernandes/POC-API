package io.github.tainafernandes.POCAPI.api.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;

public class ApiErrors {
    private List<String> errors;

    public ApiErrors(BindingResult bindingResult){
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public List<String> getErrors(){ //create get errors to return in the API
        return errors;
    }
}
