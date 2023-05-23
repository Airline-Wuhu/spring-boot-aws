package com.joshfullstack.customer;

import com.joshfullstack.exception.DuplicateResourceException;
import com.joshfullstack.exception.InvalidUpdateException;
import com.joshfullstack.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {

        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomer() {
        underTest.getAllCustomer();
        Mockito.verify(customerDAO).selectAllCustomers();
    }

    @Test
    void getCustomerByID() {
        int id = 10;
        Customer customer = new Customer(id, "name", "email", 19);
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        Customer actual = underTest.getCustomerByID(id);
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void getCustomerByIDError() {
        int id = 10;
        //Customer customer = new Customer(id, "name", "email", 19);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getCustomerByID(id)).isInstanceOf(ResourceNotFound.class).hasMessage("this id not found");

    }

    @Test
    void addCustomer() {

        String email = "email";
        Mockito.when(customerDAO.checkEmailExist(email)).thenReturn(false);

        underTest.addCustomer(new CustomerRegistrationRequest("name", email, 93));
        ArgumentCaptor<Customer> cap = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDAO).insertCustomer(cap.capture());
        Customer cappedCustomer = cap.getValue();
        assertThat(cappedCustomer.getId()).isNull();
        assertThat(cappedCustomer.getName()).isEqualTo("name");
        assertThat(cappedCustomer.getEmail()).isEqualTo(email);
        assertThat(cappedCustomer.getAge()).isEqualTo(93);

    }
@Test
    void addCustomerError() {

        String email = "email";
        Mockito.when(customerDAO.checkEmailExist(email)).thenReturn(true);
        assertThatThrownBy(() -> underTest.addCustomer(new CustomerRegistrationRequest("name", email, 93))).isInstanceOf(DuplicateResourceException.class).hasMessage("this email has been used");
        Mockito.verify(customerDAO, Mockito.never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        int id = 100;
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);
        underTest.deleteCustomer(id);
        Mockito.verify(customerDAO).deleteCustomer(id);

    }
    @Test
    void deleteCustomerError() {
        int id = 100;
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.deleteCustomer(id)).isInstanceOf(ResourceNotFound.class).hasMessage("This ID does not exist in the system");
        Mockito.verify(customerDAO, Mockito.never()).deleteCustomer(any());
    }

    @Test
    void updateCustomerIDError() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "", 10);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest)).isInstanceOf(ResourceNotFound.class).hasMessage("This ID does not exist in the system");

    }

    @Test
    void updateCustomerNoUpdateError() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "", 10);
        Customer dommy = new Customer("", "", 10);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(dommy));
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest)).isInstanceOf(InvalidUpdateException.class).hasMessage("the info provided is the same as the current record");

    }
    @Test
    void updateCustomerDupEmailError() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "abc", 10);
        Customer dommy = new Customer("", "", 10);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);
        Mockito.when(customerDAO.checkEmailExist(customerUpdateRequest.email())).thenReturn(true);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(dommy));
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest)).isInstanceOf(DuplicateResourceException.class).hasMessage("this email is already taken");

    }
    @Test
    void updateCustomerName() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "", 10);
        Customer dommy = new Customer("A", "", 10);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(dommy));
        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> cap = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDAO).updateCustomer(cap.capture());
        Customer cappedCustomer = cap.getValue();

        assertThat(cappedCustomer.getName()).isEqualTo("");
        assertThat(cappedCustomer.getEmail()).isEqualTo("");
        assertThat(cappedCustomer.getAge()).isEqualTo(10);
        Mockito.verify(customerDAO).updateCustomer(dommy);

    }

    @Test
    void updateCustomerEmail() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "", 10);
        Customer dommy = new Customer("", "A", 10);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);
        Mockito.when(customerDAO.checkEmailExist("")).thenReturn(false);
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(dommy));
        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> cap = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDAO).updateCustomer(cap.capture());
        Customer cappedCustomer = cap.getValue();

        assertThat(cappedCustomer.getName()).isEqualTo("");
        assertThat(cappedCustomer.getEmail()).isEqualTo("");
        assertThat(cappedCustomer.getAge()).isEqualTo(10);
        Mockito.verify(customerDAO).updateCustomer(dommy);

    }
    @Test
    void updateCustomerAge() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "", 10);
        Customer dommy = new Customer("", "", 20);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(dommy));
        underTest.updateCustomer(id, customerUpdateRequest);
        ArgumentCaptor<Customer> cap = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDAO).updateCustomer(cap.capture());
        Customer cappedCustomer = cap.getValue();

        assertThat(cappedCustomer.getName()).isEqualTo("");
        assertThat(cappedCustomer.getEmail()).isEqualTo("");
        assertThat(cappedCustomer.getAge()).isEqualTo(10);
        Mockito.verify(customerDAO).updateCustomer(dommy);

    }
    @Test
    void updateCustomerAll() {
        Integer id = 100;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("", "", 10);
        Customer dommy = new Customer("A", "B", 20);
        Mockito.when(customerDAO.checkIdExist(id)).thenReturn(true);
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(dommy));
        underTest.updateCustomer(id, customerUpdateRequest);
        ArgumentCaptor<Customer> cap = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDAO).updateCustomer(cap.capture());
        Customer cappedCustomer = cap.getValue();

        assertThat(cappedCustomer.getName()).isEqualTo("");
        assertThat(cappedCustomer.getEmail()).isEqualTo("");
        assertThat(cappedCustomer.getAge()).isEqualTo(10);
        Mockito.verify(customerDAO).updateCustomer(dommy);

    }
}