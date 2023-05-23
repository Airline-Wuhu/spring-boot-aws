package com.joshfullstack.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomer();
    }
    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") Integer customerId) {

        return customerService.getCustomerByID(customerId);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PutMapping("{id}")
    public void updateCustomer(@PathVariable("id") Integer customerId, @RequestBody CustomerUpdateRequest request) {
        customerService.updateCustomer(customerId, request);
    }
}
