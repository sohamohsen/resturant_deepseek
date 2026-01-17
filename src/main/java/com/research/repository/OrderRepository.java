package com.research.repository;

import com.research.model.Order;
import com.research.model.OrderStatus;

import java.util.List;

public interface OrderRepository extends Repository<Order> {
    List<Order> findByCustomer(int customerId);
    List<Order> findByStatus(OrderStatus status);
}