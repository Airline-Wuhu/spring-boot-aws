package com.joshfullstack.customer;

import com.joshfullstack.AbstractContainerUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.assertj.ApplicationContextAssert;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractContainerUnitTest {

    @Autowired
    private CustomerRepository test;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        test.deleteAll();
        //System.out.println(applicationContext.getBeanDefinitionCount());

    }

    @Test
    void existsCustomerByEmail() {

        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(19, 99)
        );
        assertThat(test.existsCustomerByEmail(email)).isFalse();
        test.save(customer);
        assertThat(test.existsCustomerByEmail(email)).isTrue();
    }
}