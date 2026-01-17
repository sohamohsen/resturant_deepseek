package com.research.service;

import com.research.exception.NotFoundException;
import com.research.exception.ValidationException;
import com.research.model.Customer;
import com.research.model.MenuItem;
import com.research.model.Order;
import com.research.model.OrderStatus;
import com.research.repository.CustomerRepository;
import com.research.repository.MenuItemRepository;
import com.research.repository.OrderRepository;

import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerService customerService;
    private final ValidationService validationService;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        MenuItemRepository menuItemRepository,
                        CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerService = customerService;
        this.validationService = new ValidationService();
    }

    public Order createOrder(int customerId, List<Integer> menuItemIds) {
        validateOrderCreation(customerId, menuItemIds);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer with ID " + customerId + " not found"));

        List<MenuItem> items = menuItemIds.stream()
                .map(id -> menuItemRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Menu item with ID " + id + " not found")))
                .toList();

        // Check if all items are available
        List<MenuItem> unavailableItems = items.stream()
                .filter(item -> !item.isAvailable())
                .toList();

        if (!unavailableItems.isEmpty()) {
            throw new ValidationException("Cannot order unavailable items: " +
                    unavailableItems.stream().map(MenuItem::getName).toList());
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.getItems().addAll(items);
        order.recalculateTotal();
        order.setStatus(OrderStatus.PENDING);

        orderRepository.add(order);

        // Add loyalty points
        customerService.addLoyaltyPoints(customerId, order.getTotalAmount());

        return order;
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.update(order);
    }

    public void addItemToOrder(int orderId, int menuItemId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PREPARING) {
            throw new ValidationException("Cannot modify order in status: " + order.getStatus());
        }

        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException("Menu item with ID " + menuItemId + " not found"));

        if (!item.isAvailable()) {
            throw new ValidationException("Menu item " + item.getName() + " is not available");
        }

        order.addItem(item);
        orderRepository.update(order);
    }

    public void removeItemFromOrder(int orderId, int menuItemId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PREPARING) {
            throw new ValidationException("Cannot modify order in status: " + order.getStatus());
        }

        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException("Menu item with ID " + menuItemId + " not found"));

        order.removeItem(item);
        orderRepository.update(order);
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with ID " + id + " not found"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderRepository.findByCustomer(customerId);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public double calculateOrderTotal(int orderId) {
        Order order = getOrderById(orderId);
        return order.getTotalAmount();
    }

    private void validateOrderCreation(int customerId, List<Integer> menuItemIds) {
        if (menuItemIds == null || menuItemIds.isEmpty()) {
            throw new ValidationException("Order must contain at least one item");
        }

        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer with ID " + customerId + " not found");
        }
    }
}