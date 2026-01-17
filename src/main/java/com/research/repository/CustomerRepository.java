package com.research.repository;

import com.research.model.Customer;

import java.util.List;

public interface CustomerRepository extends Repository<Customer> {
    List<Customer> findByName(String name);
    List<Customer> findByEmail(String email);
}