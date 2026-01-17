package com.research.service;

import com.research.exception.NotFoundException;
import com.research.model.Customer;
import com.research.repository.CustomerRepository;

import java.util.List;

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationService validationService;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.validationService = new ValidationService();
    }

    public void addCustomer(Customer customer) {
        validateCustomer(customer);
        customerRepository.add(customer);
    }

    public void updateCustomer(Customer customer) {
        if (!customerRepository.existsById(customer.getId())) {
            throw new NotFoundException("Customer with ID " + customer.getId() + " not found");
        }
        validateCustomer(customer);
        customerRepository.update(customer);
    }

    public void deleteCustomer(int id) {
        customerRepository.delete(id);
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with ID " + id + " not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByName(name);
    }

    public List<Customer> searchCustomersByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void addLoyaltyPoints(int customerId, double orderAmount) {
        Customer customer = getCustomerById(customerId);
        int points = (int) (orderAmount * 0.1); // 1 point per $10
        customer.addLoyaltyPoints(points);
        customerRepository.update(customer);
    }

    private void validateCustomer(Customer customer) {
        validationService.validateString(customer.getFullName(), "Full name");
        validationService.validateEmail(customer.getEmail());
        validationService.validatePhone(customer.getPhone());
    }
}