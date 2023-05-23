package com.joshfullstack.customer;

import com.joshfullstack.AbstractContainerUnitTest;
import com.joshfullstack.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
;


class CustomerJDBCDataAccessServiceTest extends AbstractContainerUnitTest {
    private CustomerJDBCDataAccessService customerJDBCDataAccessService;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    @BeforeEach
    void setUp() {
        customerJDBCDataAccessService = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
            FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.number().numberBetween(19, 99)
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        List<Customer> customers = customerJDBCDataAccessService.selectAllCustomers();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(19, 99)
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        int id = customerJDBCDataAccessService.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        Optional<Customer> customer1 = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(customer1).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void selectCustomerByIdError() {
        int id = 0;
        Optional<Customer> customer = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(customer).isEmpty();
    }

    @Test
    void insertCustomer() {
        String email = FAKER.internet().safeEmailAddress();

        String name = FAKER.name().fullName();
        int age = FAKER.number().numberBetween(19, 99);
        Customer customer = new Customer(
                name,
                email,
                age
        );
        Optional<Customer> first1 = customerJDBCDataAccessService.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).findFirst();
        assertThat(first1).isEmpty();
        customerJDBCDataAccessService.insertCustomer(customer);
        Optional<Customer> first = customerJDBCDataAccessService.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).findFirst();
        assertThat(name).isEqualTo(first.get().getName());
        assertThat(email).isEqualTo(first.get().getEmail());
        assertThat(age).isEqualTo(first.get().getAge());


    }

    @Test
    void checkEmailExist() {
        String email = FAKER.internet().safeEmailAddress();

        String name = FAKER.name().fullName();
        int age = FAKER.number().numberBetween(19, 99);
        Customer customer = new Customer(
                name,
                email,
                age
        );
        // Optional<Customer> first1 = customerJDBCDataAccessService.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).findFirst();
        boolean b = customerJDBCDataAccessService.checkEmailExist(email);
        assertThat(b).isFalse();
        customerJDBCDataAccessService.insertCustomer(customer);
        b = customerJDBCDataAccessService.checkEmailExist(email);
        assertThat(b).isTrue();

    }

    @Test
    void checkIdExist() {
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        int age = FAKER.number().numberBetween(19, 99);
        Customer customer = new Customer(
                name,
                email,
                age
        );
        // Optional<Customer> first1 = customerJDBCDataAccessService.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).findFirst();
        boolean b = customerJDBCDataAccessService.checkIdExist(0);
        assertThat(b).isFalse();
        customerJDBCDataAccessService.insertCustomer(customer);
        b = customerJDBCDataAccessService.checkEmailExist(email);
        assertThat(b).isTrue();
        Integer id = customerJDBCDataAccessService.selectAllCustomers().stream().filter(a -> {
            return a.getEmail().equals(email);
        }).findFirst().get().getId();
        assertThat(customerJDBCDataAccessService.checkIdExist(id)).isTrue();
    }

    @Test
    void deleteCustomer() {
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        int age = FAKER.number().numberBetween(19, 99);
        Customer customer = new Customer(
                name,
                email,
                age
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        assertThat(customerJDBCDataAccessService.checkEmailExist(email)).isTrue();
        Integer id = customerJDBCDataAccessService.selectAllCustomers().stream().filter(a -> {
            return a.getEmail().equals(email);
        }).findFirst().get().getId();
        customerJDBCDataAccessService.deleteCustomer(id);
        assertThat(customerJDBCDataAccessService.checkEmailExist(email)).isFalse();

    }
    /*
    @Test
    void deleteCustomerError() {
        int id = 0;
        assertThatThrownBy(() -> {customerJDBCDataAccessService.deleteCustomer(id);}).isInstanceOf(ResourceNotFound.class)
        ;
    }

     */
    @Test
    void updateCustomer() {
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        int age = FAKER.number().numberBetween(19, 99);
        Customer customer = new Customer(
                name,
                email,
                age
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        assertThat(customerJDBCDataAccessService.checkEmailExist(email)).isTrue();
        Integer id = customerJDBCDataAccessService.selectAllCustomers().stream().filter(a -> {
            return a.getEmail().equals(email);
        }).findFirst().get().getId();
        String email2 = "beta" + email;
        Customer update = new Customer(id, name, email2, age);

        customerJDBCDataAccessService.updateCustomer(update);
        Optional<Customer> customer1 = customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(customer1.get().getName()).isEqualTo(name);
        assertThat(customer1.get().getEmail()).isEqualTo(email2);
        assertThat(customer1.get().getAge()).isEqualTo(age);



    }
}