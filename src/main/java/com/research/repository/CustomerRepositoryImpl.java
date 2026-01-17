package com.research.repository;

import com.research.model.Customer;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerRepositoryImpl extends BaseRepository<Customer> implements CustomerRepository {

    @Override
    protected int getId(Customer entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Customer entity, int id) {
        entity.setId(id);
    }

    @Override
    public List<Customer> findByName(String name) {
        return entities.values().stream()
                .filter(customer -> customer.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findByEmail(String email) {
        return entities.values().stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }
}