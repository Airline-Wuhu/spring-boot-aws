package com.joshfullstack.customer;

import com.joshfullstack.exception.ResourceNotFound;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDAO {
    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);

    }

    @Override
    public boolean checkEmailExist(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean checkIdExist(Integer ID) {
        return customerRepository.existsById(ID);
    }

    @Override
    public void deleteCustomer(Integer ID) {
        customerRepository.deleteById(ID);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }


}
