package com.joshfullstack.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")

public class CustomerJDBCDataAccessService implements CustomerDAO{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age 
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
                SELECT * FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId).stream().findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age) VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("update = " + result);
    }

    @Override
    public boolean checkEmailExist(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        int num = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return num == 1;
    }

    @Override
    public boolean checkIdExist(Integer ID) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        int num = jdbcTemplate.queryForObject(sql, Integer.class, ID);
        return num == 1;
    }

    @Override
    public void deleteCustomer(Integer ID) {
        var sql = """
                DELETE FROM customer
                 WHERE id= ?
                 """;
        jdbcTemplate.update(sql, ID);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null) {
            var sql = """
                UPDATE customer
                SET name = ?
                WHERE id = ?
                """;
            int res = jdbcTemplate.update(sql, customer.getName(), customer.getId());
            System.out.println("the name of this customer has been updated to " + customer.getName() + "status: " + res);
        }
        if (customer.getEmail() != null) {
            var sql = """
                UPDATE customer
                SET email = ?
                WHERE id = ?
                """;
            int res = jdbcTemplate.update(sql,customer.getEmail(), customer.getId());
            System.out.println("the email of this customer has been updated to " + customer.getEmail() + "status: " + res);
        }
        if (customer.getAge() != null) {
            var sql = """
                UPDATE customer
                SET age = ?
                WHERE id = ?
                """;
            int res = jdbcTemplate.update(sql, customer.getAge(), customer.getId());
            System.out.println("the age of this customer has been updated to " + customer.getAge() + "status: " + res);
        }


    }
}
