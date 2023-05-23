package com.joshfullstack.customer;

import com.joshfullstack.exception.ResourceNotFound;
import org.springframework.stereotype.Repository;

import javax.management.relation.RelationServiceNotRegisteredException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListAccessService implements CustomerDAO{
    private static List<Customer> customers;
    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1,"Alex",  "alex@mail.com", 21);
        customers.add(alex);
        Customer bob = new Customer(2,"Bob",  "bob@mail.com", 12);
        customers.add(bob);

    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream()
                .filter(person -> person.getId().equals(customerId))
                .findFirst()
                ;
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean checkEmailExist(String email) {
        return customers.stream().anyMatch(a -> {
            return a.getEmail().equals(email);
        });
    }

    @Override
    public boolean checkIdExist(Integer ID) {
        return customers.stream().anyMatch(a -> {
            return a.getId().equals(ID);
        });
    }

    @Override
    public void deleteCustomer(Integer ID) {
        /*
        for (Customer c: customers) {
            if (c.getId() == ID) {
                customers.remove(c);
                return;
            }
        }

         */
        customers.stream().filter(a -> a.getId().equals(ID)).findFirst().ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }
}
