package com.joshfullstack.customer;

import com.joshfullstack.exception.DuplicateResourceException;
import com.joshfullstack.exception.InvalidUpdateException;
import com.joshfullstack.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;
    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomer() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomerByID(Integer customerID) {
        return customerDAO.selectCustomerById(customerID).orElseThrow(() -> new ResourceNotFound("this id not found"));
    }
    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (!customerDAO.checkEmailExist(customerRegistrationRequest.email())) {
            Customer newCus = new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(), customerRegistrationRequest.age());
            customerDAO.insertCustomer(newCus);
        } else {
            throw new DuplicateResourceException("this email has been used");
        }

    }
    public void deleteCustomer(Integer ID) {
        if (!customerDAO.checkIdExist(ID)) throw new ResourceNotFound("This ID does not exist in the system");
        customerDAO.deleteCustomer(ID);
    }

    public void updateCustomer(Integer ID, CustomerUpdateRequest customerUpdateRequest) {
        if (!customerDAO.checkIdExist(ID)) throw new ResourceNotFound("This ID does not exist in the system");
        boolean check = false;
        Customer customer = getCustomerByID(ID);
        if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            check = true;
            customer.setName(customerUpdateRequest.name());
        }
        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (customerDAO.checkEmailExist(customerUpdateRequest.email())) {
                throw new DuplicateResourceException("this email is already taken");
            }
            check = true;
            customer.setEmail(customerUpdateRequest.email());
        }
        if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            check = true;
            customer.setAge(customerUpdateRequest.age());
        }
        if (!check) throw new InvalidUpdateException("the info provided is the same as the current record");
        customerDAO.updateCustomer(customer);
    }
}
