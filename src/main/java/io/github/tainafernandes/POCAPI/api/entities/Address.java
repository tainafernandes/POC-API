package io.github.tainafernandes.POCAPI.api.entities;

import io.github.tainafernandes.POCAPI.api.enums.StateAbbreviations;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String zipCode;
    @Column
    private String city;
    @Column
    @Enumerated(EnumType.STRING)
    private StateAbbreviations state;
    @Column
    private String neighborhood;
    @Column
    private String street;
    @Column
    private String addressNumber;
    @Column
    private String complement;
}
