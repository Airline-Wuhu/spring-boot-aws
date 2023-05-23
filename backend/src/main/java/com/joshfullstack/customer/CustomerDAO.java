package com.joshfullstack.customer;


import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);

    void insertCustomer(Customer customer);
    boolean checkEmailExist(String email);
    boolean checkIdExist(Integer ID);

    void deleteCustomer(Integer ID);
    void updateCustomer(Customer customer);
}
