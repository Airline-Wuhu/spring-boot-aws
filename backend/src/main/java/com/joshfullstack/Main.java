package com.joshfullstack;

import com.github.javafaker.Faker;
import com.joshfullstack.customer.Customer;
import com.joshfullstack.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            Random random = new Random();

            Customer customer = new Customer(
                    faker.name().fullName(),
                    faker.internet().safeEmailAddress(),
                    random.nextInt(16, 99)
            );
            customerRepository.save(customer);
        };
    }


}
