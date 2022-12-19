package io.github.tainafernandes.POCAPI.api.enums;

import io.github.tainafernandes.POCAPI.api.validation.CnpjGroup;
import io.github.tainafernandes.POCAPI.api.validation.CpfGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum documentType {
    PF("000.000.000-00", CpfGroup.class),
    PJ("00.000.000/0000-00", CnpjGroup.class);

    private final String mask;
    private final Class<?> group;
}
