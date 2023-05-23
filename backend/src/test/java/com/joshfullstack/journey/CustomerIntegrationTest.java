package com.joshfullstack.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.joshfullstack.customer.Customer;
import com.joshfullstack.customer.CustomerController;
import com.joshfullstack.customer.CustomerRegistrationRequest;
import com.joshfullstack.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;



    @Test
    void canRegisterCustomer() {
        Faker faker = new Faker();
        Name fullname = faker.name();
        String name = fullname.fullName();
        String email = fullname.lastName() + fullname.lastName() + "@bizzaremail.com";
        int age = faker.number().numberBetween(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name,email, age
        );
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        List<Customer> customers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();
        Customer expectedCustomer = new Customer(
                name, email, age
        );
        assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        //by id
        int id = customers.stream().filter(c ->
                c.getEmail().equals(expectedCustomer.getEmail())).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);
        webTestClient.get().uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).isEqualTo(expectedCustomer);
    }
    @Test
    void canDeleteCustomer() {
        Faker faker = new Faker();
        Name fullname = faker.name();
        String name = fullname.fullName();
        String email = fullname.lastName() + fullname.lastName() + "@bizzaremail.com";
        int age = faker.number().numberBetween(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name,email, age
        );
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        List<Customer> customers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();

        //by id
        int id = customers.stream().filter(c ->
                c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        webTestClient.delete().uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get().uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    void canUpdateCustomer() {
        Faker faker = new Faker();
        Name fullname = faker.name();
        String name = fullname.fullName();
        String email = fullname.lastName() + fullname.lastName() + "@bizzaremail.com";
        int age = faker.number().numberBetween(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name,email, age
        );
        webTestClient.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        List<Customer> customers = webTestClient.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult().getResponseBody();

        //by id
        int id = customers.stream().filter(c ->
                c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        //update
        String newName = "newName";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null
        );
        webTestClient.put().uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Customer responseBody = webTestClient.get().uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();
        Customer customer = new Customer(
                id, newName, email, age
        );

        assertThat(responseBody).isEqualTo(customer);
    }

}
