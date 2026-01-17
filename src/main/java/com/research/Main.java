package com.research;

import com.research.repository.*;
import com.research.service.*;
import com.research.ui.ConsoleUI;


public class Main {
    public static void main(String[] args) {
        try {
            // Initialize repositories
            StaffRepository staffRepository = new StaffRepositoryImpl();
            MenuCategoryRepository categoryRepository = new MenuCategoryRepositoryImpl();
            MenuItemRepository menuItemRepository = new MenuItemRepositoryImpl();
            CustomerRepository customerRepository = new CustomerRepositoryImpl();
            TableRepository tableRepository = new TableRepositoryImpl();
            OrderRepository orderRepository = new OrderRepositoryImpl();
            ReservationRepository reservationRepository = new ReservationRepositoryImpl();

            // Initialize services
            StaffService staffService = new StaffService(staffRepository);
            MenuCategoryService categoryService = new MenuCategoryService(categoryRepository);
            MenuItemService menuItemService = new MenuItemService(menuItemRepository, categoryRepository);
            CustomerService customerService = new CustomerService(customerRepository);
            TableService tableService = new TableService(tableRepository);
            OrderService orderService = new OrderService(orderRepository, customerRepository,
                    menuItemRepository, customerService);
            ReservationService reservationService = new ReservationService(reservationRepository,
                    customerRepository, tableRepository, tableService);

            // Initialize and start UI
            ConsoleUI consoleUI = new ConsoleUI(staffService, categoryService, menuItemService,
                    customerService, tableService, orderService, reservationService);

            consoleUI.start();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}