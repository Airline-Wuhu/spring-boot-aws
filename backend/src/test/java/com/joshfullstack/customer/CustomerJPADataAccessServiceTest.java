package com.joshfullstack.customer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService test;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        test = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        test.selectAllCustomers();
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        test.selectCustomerById(0);
        Mockito.verify(customerRepository).findById(0);

    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer();
        test.insertCustomer(customer);
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void checkEmailExist() {

        test.checkEmailExist("email");
        Mockito.verify(customerRepository).existsCustomerByEmail("email");

    }

    @Test
    void checkIdExist() {
        test.checkIdExist(0);
        Mockito.verify(customerRepository).existsById(0);
    }

    @Test
    void deleteCustomer() {
        test.deleteCustomer(0);
        Mockito.verify(customerRepository).deleteById(0);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer();
        test.updateCustomer(customer);
        Mockito.verify(customerRepository).save(customer);
    }
}