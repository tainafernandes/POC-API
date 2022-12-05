package io.github.tainafernandes.POCAPI.api.repository;

import io.github.tainafernandes.POCAPI.api.entities.Customer;
import io.github.tainafernandes.POCAPI.api.enums.documentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //indicates that it will test with JPA - DB in memory
public class CustomerRepositoryTest { //Integration Test

    @Autowired
    TestEntityManager entityManager; //used to create scenery
    @Autowired
    CustomerRepository repository;

    @Test
    @DisplayName("It must return true when there is a Customer in the base with the informed Document")
    public void returnTrueWhenDocumentExists(){
        //scenery
        String document = "12345678900";
        Customer customer = Customer.builder().name("Josefa").email("josefa@email.com")
                .document(document).documentType(documentType.PF).phoneNumber("11999999999")
                .build();
        entityManager.persist(customer); //vai persistir uma entidade

        //execution
        boolean exists =  repository.existsByDocument(document);

        //verification
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("It must return false when not there is a Customer in the base with the informed Document")
    public void returnFalseWhenDocumentDoesntExists(){
        //scenery
        String document = "12345678900";

        //execution
        boolean exists =  repository.existsByDocument(document);

        //verification
        assertThat(exists).isFalse();
    }

}
