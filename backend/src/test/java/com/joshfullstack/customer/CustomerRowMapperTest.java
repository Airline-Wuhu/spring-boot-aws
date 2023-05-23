package com.joshfullstack.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;


class CustomerRowMapperTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void mapRow() throws SQLException {
        ResultSet mock = mock(ResultSet.class);
        int id = 0;
        String name = "name";
        String email = "email";
        int age = 0;
        Mockito.when(mock.getInt("id")).thenReturn(id);
        Mockito.when(mock.getString("name")).thenReturn(name);
        Mockito.when(mock.getString("email")).thenReturn(email);
        Mockito.when(mock.getInt("age")).thenReturn(age);
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        Customer customer = customerRowMapper.mapRow(mock, 1);
        assertThat(customer.getId()).isEqualTo(id);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getAge()).isEqualTo(age);
    }
}