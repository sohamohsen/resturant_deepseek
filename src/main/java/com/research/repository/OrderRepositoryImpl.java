package com.research.repository;

import com.research.model.Order;
import com.research.model.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRepositoryImpl extends BaseRepository<Order> implements OrderRepository {

    @Override
    protected int getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Order entity, int id) {
        entity.setId(id);
    }

    @Override
    public List<Order> findByCustomer(int customerId) {
        return entities.values().stream()
                .filter(order -> order.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return entities.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }
}