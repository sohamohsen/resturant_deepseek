package com.research.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private Customer customer;
    private List<MenuItem> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;

    // Add no-argument constructor
    public Order() {
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    // Keep existing constructor
    public Order(int id, Customer customer) {
        this(); // Call no-arg constructor first
        this.id = id;
        this.customer = customer;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        recalculateTotal();
    }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public void addItem(MenuItem item) {
        if (item.isAvailable()) {
            items.add(item);
            recalculateTotal();
        }
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
        recalculateTotal();
    }

    // Make this public so OrderService can call it
    public void recalculateTotal() {
        double subtotal = items.stream().mapToDouble(MenuItem::getPrice).sum();
        this.totalAmount = subtotal + (subtotal * 0.10); // Add 10% service charge
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, customer='%s', totalAmount=%.2f, status=%s, items=%d}",
                id, customer.getFullName(), totalAmount, status, items.size());
    }
}