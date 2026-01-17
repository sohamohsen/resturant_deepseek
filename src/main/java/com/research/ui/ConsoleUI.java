package com.research.ui;

import com.research.model.*;
import com.research.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final Scanner scanner;
    private final StaffService staffService;
    private final MenuCategoryService categoryService;
    private final MenuItemService menuItemService;
    private final CustomerService customerService;
    private final TableService tableService;
    private final OrderService orderService;
    private final ReservationService reservationService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ConsoleUI(StaffService staffService,
                     MenuCategoryService categoryService,
                     MenuItemService menuItemService,
                     CustomerService customerService,
                     TableService tableService,
                     OrderService orderService,
                     ReservationService reservationService) {
        this.scanner = new Scanner(System.in);
        this.staffService = staffService;
        this.categoryService = categoryService;
        this.menuItemService = menuItemService;
        this.customerService = customerService;
        this.tableService = tableService;
        this.orderService = orderService;
        this.reservationService = reservationService;
    }

    public void start() {
        initializeSampleData();

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> manageStaff();
                    case 2 -> manageMenuCategories();
                    case 3 -> manageMenuItems();
                    case 4 -> manageCustomers();
                    case 5 -> manageTables();
                    case 6 -> manageOrders();
                    case 7 -> manageReservations();
                    case 8 -> displayReports();
                    case 0 -> {
                        System.out.println("Thank you for using Restaurant Management System!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== RESTAURANT MANAGEMENT SYSTEM ===");
        System.out.println("1. Staff Management");
        System.out.println("2. Menu Category Management");
        System.out.println("3. Menu Item Management");
        System.out.println("4. Customer Management");
        System.out.println("5. Table Management");
        System.out.println("6. Order Management");
        System.out.println("7. Reservation Management");
        System.out.println("8. Reports & Analytics");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }

    private void manageStaff() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== STAFF MANAGEMENT ===");
            System.out.println("1. Add Staff");
            System.out.println("2. View All Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. Delete Staff");
            System.out.println("5. Search Staff by Name");
            System.out.println("6. Search Staff by Role");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addStaff();
                case 2 -> viewAllStaff();
                case 3 -> updateStaff();
                case 4 -> deleteStaff();
                case 5 -> searchStaffByName();
                case 6 -> searchStaffByRole();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addStaff() {
        System.out.println("\n--- Add New Staff ---");
        int id = getIntInput("Enter ID (0 for auto-generate): ");
        String fullName = getStringInput("Enter full name: ");
        String role = getStringInput("Enter role (Waiter, Chef, Manager, etc.): ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");

        Staff staff = new Staff(id, fullName, role, email, phone);
        staffService.addStaff(staff);
        System.out.println("Staff added successfully!");
    }

    private void viewAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        if (staffList.isEmpty()) {
            System.out.println("No staff records found.");
        } else {
            System.out.println("\n--- All Staff ---");
            staffList.forEach(System.out::println);
        }
    }

    private void updateStaff() {
        int id = getIntInput("Enter staff ID to update: ");
        try {
            Staff staff = staffService.getStaffById(id);

            System.out.println("Current details: " + staff);
            System.out.println("Enter new details (leave blank to keep current):");

            String fullName = getStringInputOrKeep("Enter full name [" + staff.getFullName() + "]: ", staff.getFullName());
            String role = getStringInputOrKeep("Enter role [" + staff.getRole() + "]: ", staff.getRole());
            String email = getStringInputOrKeep("Enter email [" + staff.getEmail() + "]: ", staff.getEmail());
            String phone = getStringInputOrKeep("Enter phone [" + staff.getPhone() + "]: ", staff.getPhone());

            staff.setFullName(fullName);
            staff.setRole(role);
            staff.setEmail(email);
            staff.setPhone(phone);

            staffService.updateStaff(staff);
            System.out.println("Staff updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteStaff() {
        int id = getIntInput("Enter staff ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            staffService.deleteStaff(id);
            System.out.println("Staff deleted successfully!");
        }
    }

    private void searchStaffByName() {
        String name = getStringInput("Enter name to search: ");
        List<Staff> results = staffService.searchStaffByName(name);
        if (results.isEmpty()) {
            System.out.println("No staff found with name containing: " + name);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private void searchStaffByRole() {
        String role = getStringInput("Enter role to search: ");
        List<Staff> results = staffService.searchStaffByRole(role);
        if (results.isEmpty()) {
            System.out.println("No staff found with role: " + role);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private void manageMenuCategories() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU CATEGORY MANAGEMENT ===");
            System.out.println("1. Add Category");
            System.out.println("2. View All Categories");
            System.out.println("3. Update Category");
            System.out.println("4. Delete Category");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addMenuCategory();
                case 2 -> viewAllCategories();
                case 3 -> updateMenuCategory();
                case 4 -> deleteMenuCategory();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addMenuCategory() {
        System.out.println("\n--- Add New Menu Category ---");
        int id = getIntInput("Enter ID (0 for auto-generate): ");
        String name = getStringInput("Enter category name: ");
        String description = getStringInput("Enter category description: ");

        MenuCategory category = new MenuCategory(id, name, description);
        categoryService.addCategory(category);
        System.out.println("Category added successfully!");
    }

    private void viewAllCategories() {
        List<MenuCategory> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("\n--- All Categories ---");
            categories.forEach(System.out::println);
        }
    }

    private void updateMenuCategory() {
        int id = getIntInput("Enter category ID to update: ");
        try {
            MenuCategory category = categoryService.getCategoryById(id);

            System.out.println("Current details: " + category);
            System.out.println("Enter new details (leave blank to keep current):");

            String name = getStringInputOrKeep("Enter name [" + category.getName() + "]: ", category.getName());
            String description = getStringInputOrKeep("Enter description [" + category.getDescription() + "]: ", category.getDescription());

            category.setName(name);
            category.setDescription(description);

            categoryService.updateCategory(category);
            System.out.println("Category updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteMenuCategory() {
        int id = getIntInput("Enter category ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            categoryService.deleteCategory(id);
            System.out.println("Category deleted successfully!");
        }
    }

    private void manageMenuItems() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU ITEM MANAGEMENT ===");
            System.out.println("1. Add Menu Item");
            System.out.println("2. View All Menu Items");
            System.out.println("3. Update Menu Item");
            System.out.println("4. Delete Menu Item");
            System.out.println("5. Search Menu Items");
            System.out.println("6. View Available Items");
            System.out.println("7. View Items by Category");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addMenuItem();
                case 2 -> viewAllMenuItems();
                case 3 -> updateMenuItem();
                case 4 -> deleteMenuItem();
                case 5 -> searchMenuItems();
                case 6 -> viewAvailableItems();
                case 7 -> viewItemsByCategory();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addMenuItem() {
        System.out.println("\n--- Add New Menu Item ---");
        viewAllCategories();

        int id = getIntInput("Enter ID (0 for auto-generate): ");
        String name = getStringInput("Enter item name: ");
        String description = getStringInput("Enter item description: ");
        double price = getDoubleInput("Enter price: ");
        int categoryId = getIntInput("Enter category ID: ");
        boolean isAvailable = getYesNoInput("Is item available? (yes/no): ");

        try {
            MenuCategory category = categoryService.getCategoryById(categoryId);
            MenuItem item = new MenuItem(id, name, description, price, category, isAvailable);
            menuItemService.addMenuItem(item);
            System.out.println("Menu item added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllMenuItems() {
        List<MenuItem> items = menuItemService.getAllMenuItems();
        if (items.isEmpty()) {
            System.out.println("No menu items found.");
        } else {
            System.out.println("\n--- All Menu Items ---");
            items.forEach(System.out::println);
        }
    }

    private void updateMenuItem() {
        int id = getIntInput("Enter menu item ID to update: ");
        try {
            MenuItem item = menuItemService.getMenuItemById(id);

            System.out.println("Current details: " + item);
            System.out.println("Enter new details (leave blank to keep current):");

            String name = getStringInputOrKeep("Enter name [" + item.getName() + "]: ", item.getName());
            String description = getStringInputOrKeep("Enter description [" + item.getDescription() + "]: ", item.getDescription());
            String priceStr = getStringInput("Enter price [" + item.getPrice() + "] (enter to keep): ");
            double price = priceStr.isEmpty() ? item.getPrice() : Double.parseDouble(priceStr);

            System.out.println("Current category: " + item.getCategory().getName());
            String categoryIdStr = getStringInput("Enter new category ID (enter to keep): ");
            MenuCategory category = categoryIdStr.isEmpty() ? item.getCategory() :
                    categoryService.getCategoryById(Integer.parseInt(categoryIdStr));

            boolean isAvailable = getYesNoInputOrKeep("Is available? [" + (item.isAvailable() ? "yes" : "no") + "] (yes/no/keep): ", item.isAvailable());

            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setCategory(category);
            item.setAvailable(isAvailable);

            menuItemService.updateMenuItem(item);
            System.out.println("Menu item updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteMenuItem() {
        int id = getIntInput("Enter menu item ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            menuItemService.deleteMenuItem(id);
            System.out.println("Menu item deleted successfully!");
        }
    }

    private void searchMenuItems() {
        String name = getStringInput("Enter item name to search: ");
        List<MenuItem> results = menuItemService.searchMenuItemsByName(name);
        if (results.isEmpty()) {
            System.out.println("No items found with name containing: " + name);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private void viewAvailableItems() {
        List<MenuItem> items = menuItemService.getAvailableItems();
        if (items.isEmpty()) {
            System.out.println("No available items found.");
        } else {
            System.out.println("\n--- Available Menu Items ---");
            items.forEach(System.out::println);
        }
    }

    private void viewItemsByCategory() {
        viewAllCategories();
        int categoryId = getIntInput("Enter category ID: ");
        List<MenuItem> items = menuItemService.getItemsByCategory(categoryId);
        if (items.isEmpty()) {
            System.out.println("No items found in this category.");
        } else {
            System.out.println("\n--- Items in Category ---");
            items.forEach(System.out::println);
        }
    }

    private void manageCustomers() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== CUSTOMER MANAGEMENT ===");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Search Customers by Name");
            System.out.println("6. Search Customers by Email");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addCustomer();
                case 2 -> viewAllCustomers();
                case 3 -> updateCustomer();
                case 4 -> deleteCustomer();
                case 5 -> searchCustomersByName();
                case 6 -> searchCustomersByEmail();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addCustomer() {
        System.out.println("\n--- Add New Customer ---");
        int id = getIntInput("Enter ID (0 for auto-generate): ");
        String fullName = getStringInput("Enter full name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");

        Customer customer = new Customer(id, fullName, email, phone);
        customerService.addCustomer(customer);
        System.out.println("Customer added successfully!");
    }

    private void viewAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("\n--- All Customers ---");
            customers.forEach(System.out::println);
        }
    }

    private void updateCustomer() {
        int id = getIntInput("Enter customer ID to update: ");
        try {
            Customer customer = customerService.getCustomerById(id);

            System.out.println("Current details: " + customer);
            System.out.println("Enter new details (leave blank to keep current):");

            String fullName = getStringInputOrKeep("Enter full name [" + customer.getFullName() + "]: ", customer.getFullName());
            String email = getStringInputOrKeep("Enter email [" + customer.getEmail() + "]: ", customer.getEmail());
            String phone = getStringInputOrKeep("Enter phone [" + customer.getPhone() + "]: ", customer.getPhone());

            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);

            customerService.updateCustomer(customer);
            System.out.println("Customer updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        int id = getIntInput("Enter customer ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            customerService.deleteCustomer(id);
            System.out.println("Customer deleted successfully!");
        }
    }

    private void searchCustomersByName() {
        String name = getStringInput("Enter name to search: ");
        List<Customer> results = customerService.searchCustomersByName(name);
        if (results.isEmpty()) {
            System.out.println("No customers found with name containing: " + name);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private void searchCustomersByEmail() {
        String email = getStringInput("Enter email to search: ");
        List<Customer> results = customerService.searchCustomersByEmail(email);
        if (results.isEmpty()) {
            System.out.println("No customers found with email: " + email);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private void manageTables() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== TABLE MANAGEMENT ===");
            System.out.println("1. Add Table");
            System.out.println("2. View All Tables");
            System.out.println("3. Update Table");
            System.out.println("4. Delete Table");
            System.out.println("5. View Available Tables");
            System.out.println("6. View Tables by Capacity");
            System.out.println("7. Update Table Status");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addTable();
                case 2 -> viewAllTables();
                case 3 -> updateTable();
                case 4 -> deleteTable();
                case 5 -> viewAvailableTables();
                case 6 -> viewTablesByCapacity();
                case 7 -> updateTableStatus();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addTable() {
        System.out.println("\n--- Add New Table ---");
        int id = getIntInput("Enter ID (0 for auto-generate): ");
        int tableNumber = getIntInput("Enter table number: ");
        int capacity = getIntInput("Enter table capacity: ");

        Table table = new Table(id, tableNumber, capacity, TableStatus.AVAILABLE);
        tableService.addTable(table);
        System.out.println("Table added successfully!");
    }

    private void viewAllTables() {
        List<Table> tables = tableService.getAllTables();
        if (tables.isEmpty()) {
            System.out.println("No tables found.");
        } else {
            System.out.println("\n--- All Tables ---");
            tables.forEach(System.out::println);
        }
    }

    private void updateTable() {
        int id = getIntInput("Enter table ID to update: ");
        try {
            Table table = tableService.getTableById(id);

            System.out.println("Current details: " + table);
            System.out.println("Enter new details (leave blank to keep current):");

            String tableNumberStr = getStringInput("Enter table number [" + table.getTableNumber() + "] (enter to keep): ");
            int tableNumber = tableNumberStr.isEmpty() ? table.getTableNumber() : Integer.parseInt(tableNumberStr);

            String capacityStr = getStringInput("Enter capacity [" + table.getCapacity() + "] (enter to keep): ");
            int capacity = capacityStr.isEmpty() ? table.getCapacity() : Integer.parseInt(capacityStr);

            table.setTableNumber(tableNumber);
            table.setCapacity(capacity);

            tableService.updateTable(table);
            System.out.println("Table updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteTable() {
        int id = getIntInput("Enter table ID to delete: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            tableService.deleteTable(id);
            System.out.println("Table deleted successfully!");
        }
    }

    private void viewAvailableTables() {
        List<Table> tables = tableService.getAvailableTables();
        if (tables.isEmpty()) {
            System.out.println("No available tables found.");
        } else {
            System.out.println("\n--- Available Tables ---");
            tables.forEach(System.out::println);
        }
    }

    private void viewTablesByCapacity() {
        int minCapacity = getIntInput("Enter minimum capacity: ");
        List<Table> tables = tableService.getTablesByCapacity(minCapacity);
        if (tables.isEmpty()) {
            System.out.println("No tables found with capacity >= " + minCapacity);
        } else {
            System.out.println("\n--- Tables with Capacity >= " + minCapacity + " ---");
            tables.forEach(System.out::println);
        }
    }

    private void updateTableStatus() {
        int tableId = getIntInput("Enter table ID: ");
        try {
            Table table = tableService.getTableById(tableId);
            System.out.println("Current status: " + table.getStatus());

            System.out.println("Select new status:");
            System.out.println("1. AVAILABLE");
            System.out.println("2. RESERVED");
            System.out.println("3. OCCUPIED");

            int choice = getIntInput("Enter choice: ");
            TableStatus newStatus = switch (choice) {
                case 1 -> TableStatus.AVAILABLE;
                case 2 -> TableStatus.RESERVED;
                case 3 -> TableStatus.OCCUPIED;
                default -> {
                    System.out.println("Invalid choice. Keeping current status.");
                    yield table.getStatus();
                }
            };

            tableService.updateTableStatus(tableId, newStatus);
            System.out.println("Table status updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageOrders() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== ORDER MANAGEMENT ===");
            System.out.println("1. Create Order");
            System.out.println("2. View All Orders");
            System.out.println("3. View Order Details");
            System.out.println("4. Update Order Status");
            System.out.println("5. Add Item to Order");
            System.out.println("6. Remove Item from Order");
            System.out.println("7. View Orders by Customer");
            System.out.println("8. View Orders by Status");
            System.out.println("9. Calculate Order Total");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> createOrder();
                case 2 -> viewAllOrders();
                case 3 -> viewOrderDetails();
                case 4 -> updateOrderStatus();
                case 5 -> addItemToOrder();
                case 6 -> removeItemFromOrder();
                case 7 -> viewOrdersByCustomer();
                case 8 -> viewOrdersByStatus();
                case 9 -> calculateOrderTotal();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createOrder() {
        System.out.println("\n--- Create New Order ---");
        viewAllCustomers();
        viewAvailableItems();

        int customerId = getIntInput("Enter customer ID: ");

        List<Integer> itemIds = new ArrayList<>();
        boolean addingItems = true;

        while (addingItems) {
            int itemId = getIntInput("Enter menu item ID to add (0 to finish): ");
            if (itemId == 0) {
                addingItems = false;
            } else {
                itemIds.add(itemId);
            }
        }

        if (itemIds.isEmpty()) {
            System.out.println("Order must contain at least one item.");
            return;
        }

        try {
            Order order = orderService.createOrder(customerId, itemIds);
            System.out.println("Order created successfully!");
            System.out.println("Order #" + order.getId() + " | Total: $" + String.format("%.2f", order.getTotalAmount()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("\n--- All Orders ---");
            orders.forEach(System.out::println);
        }
    }

    private void viewOrderDetails() {
        int orderId = getIntInput("Enter order ID: ");
        try {
            Order order = orderService.getOrderById(orderId);
            System.out.println("\n--- Order Details ---");
            System.out.println("Order #" + order.getId());
            System.out.println("Customer: " + order.getCustomer().getFullName());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Date: " + order.getOrderDate());
            System.out.println("\nItems:");
            order.getItems().forEach(item ->
                    System.out.println("  - " + item.getName() + " ($" + String.format("%.2f", item.getPrice()) + ")"));
            System.out.println("\nSubtotal: $" + String.format("%.2f", order.getItems().stream().mapToDouble(MenuItem::getPrice).sum()));
            System.out.println("Service Charge (10%): $" + String.format("%.2f", order.getTotalAmount() - order.getItems().stream().mapToDouble(MenuItem::getPrice).sum()));
            System.out.println("Total: $" + String.format("%.2f", order.getTotalAmount()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        int orderId = getIntInput("Enter order ID: ");
        try {
            Order order = orderService.getOrderById(orderId);
            System.out.println("Current status: " + order.getStatus());

            System.out.println("Select new status:");
            System.out.println("1. PENDING");
            System.out.println("2. PREPARING");
            System.out.println("3. SERVED");
            System.out.println("4. PAID");

            int choice = getIntInput("Enter choice: ");
            OrderStatus newStatus = switch (choice) {
                case 1 -> OrderStatus.PENDING;
                case 2 -> OrderStatus.PREPARING;
                case 3 -> OrderStatus.SERVED;
                case 4 -> OrderStatus.PAID;
                default -> {
                    System.out.println("Invalid choice. Keeping current status.");
                    yield order.getStatus();
                }
            };

            orderService.updateOrderStatus(orderId, newStatus);
            System.out.println("Order status updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addItemToOrder() {
        int orderId = getIntInput("Enter order ID: ");
        viewAvailableItems();
        int itemId = getIntInput("Enter menu item ID to add: ");

        try {
            orderService.addItemToOrder(orderId, itemId);
            System.out.println("Item added to order successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeItemFromOrder() {
        int orderId = getIntInput("Enter order ID: ");
        try {
            Order order = orderService.getOrderById(orderId);
            System.out.println("Current items:");
            order.getItems().forEach(item ->
                    System.out.println("  [" + item.getId() + "] " + item.getName()));

            int itemId = getIntInput("Enter menu item ID to remove: ");
            orderService.removeItemFromOrder(orderId, itemId);
            System.out.println("Item removed from order successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewOrdersByCustomer() {
        int customerId = getIntInput("Enter customer ID: ");
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        if (orders.isEmpty()) {
            System.out.println("No orders found for this customer.");
        } else {
            System.out.println("\n--- Orders for Customer ---");
            orders.forEach(System.out::println);
        }
    }

    private void viewOrdersByStatus() {
        System.out.println("Select status:");
        System.out.println("1. PENDING");
        System.out.println("2. PREPARING");
        System.out.println("3. SERVED");
        System.out.println("4. PAID");

        int choice = getIntInput("Enter choice: ");
        OrderStatus status = switch (choice) {
            case 1 -> OrderStatus.PENDING;
            case 2 -> OrderStatus.PREPARING;
            case 3 -> OrderStatus.SERVED;
            case 4 -> OrderStatus.PAID;
            default -> {
                System.out.println("Invalid choice. Using PENDING.");
                yield OrderStatus.PENDING;
            }
        };

        List<Order> orders = orderService.getOrdersByStatus(status);
        if (orders.isEmpty()) {
            System.out.println("No orders found with status: " + status);
        } else {
            System.out.println("\n--- Orders with Status: " + status + " ---");
            orders.forEach(System.out::println);
        }
    }

    private void calculateOrderTotal() {
        int orderId = getIntInput("Enter order ID: ");
        try {
            double total = orderService.calculateOrderTotal(orderId);
            System.out.println("Order #" + orderId + " total: $" + String.format("%.2f", total));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageReservations() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== RESERVATION MANAGEMENT ===");
            System.out.println("1. Create Reservation");
            System.out.println("2. View All Reservations");
            System.out.println("3. View Reservation Details");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Check Table Availability");
            System.out.println("6. Find Available Tables for Reservation");
            System.out.println("7. View Reservations by Date");
            System.out.println("8. View Reservations by Customer");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> createReservation();
                case 2 -> viewAllReservations();
                case 3 -> viewReservationDetails();
                case 4 -> cancelReservation();
                case 5 -> checkTableAvailability();
                case 6 -> findAvailableTablesForReservation();
                case 7 -> viewReservationsByDate();
                case 8 -> viewReservationsByCustomer();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createReservation() {
        System.out.println("\n--- Create New Reservation ---");
        viewAllCustomers();
        viewAvailableTables();

        int customerId = getIntInput("Enter customer ID: ");
        int tableId = getIntInput("Enter table ID: ");

        System.out.print("Enter reservation date (YYYY-MM-DD): ");
        LocalDate date = getDateInput();

        System.out.print("Enter reservation time (HH:MM): ");
        LocalTime time = getTimeInput();

        int numberOfGuests = getIntInput("Enter number of guests: ");

        try {
            Reservation reservation = reservationService.createReservation(
                    customerId, tableId, date, time, numberOfGuests);
            System.out.println("Reservation created successfully!");
            System.out.println("Reservation #" + reservation.getId() +
                    " for " + reservation.getCustomer().getFullName() +
                    " at " + reservation.getReservationTime() + " on " +
                    reservation.getReservationDate());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("\n--- All Reservations ---");
            reservations.forEach(System.out::println);
        }
    }

    private void viewReservationDetails() {
        int reservationId = getIntInput("Enter reservation ID: ");
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            System.out.println("\n--- Reservation Details ---");
            System.out.println("Reservation #" + reservation.getId());
            System.out.println("Customer: " + reservation.getCustomer().getFullName());
            System.out.println("Table: #" + reservation.getTable().getTableNumber() +
                    " (Capacity: " + reservation.getTable().getCapacity() + ")");
            System.out.println("Date: " + reservation.getReservationDate());
            System.out.println("Time: " + reservation.getReservationTime());
            System.out.println("Number of Guests: " + reservation.getNumberOfGuests());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        int reservationId = getIntInput("Enter reservation ID to cancel: ");
        String confirm = getStringInput("Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            reservationService.cancelReservation(reservationId);
            System.out.println("Reservation cancelled successfully!");
        }
    }

    private void checkTableAvailability() {
        int tableId = getIntInput("Enter table ID: ");
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = getDateInput();
        System.out.print("Enter time (HH:MM): ");
        LocalTime time = getTimeInput();

        boolean available = reservationService.checkTableAvailability(tableId, date, time);
        if (available) {
            System.out.println("Table is available for reservation.");
        } else {
            System.out.println("Table is not available for reservation.");
        }
    }

    private void findAvailableTablesForReservation() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = getDateInput();
        System.out.print("Enter time (HH:MM): ");
        LocalTime time = getTimeInput();
        int numberOfGuests = getIntInput("Enter number of guests: ");

        List<Table> availableTables = reservationService.getAvailableTablesForReservation(
                date, time, numberOfGuests);

        if (availableTables.isEmpty()) {
            System.out.println("No available tables found for the specified criteria.");
        } else {
            System.out.println("\n--- Available Tables ---");
            availableTables.forEach(System.out::println);
        }
    }

    private void viewReservationsByDate() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = getDateInput();

        List<Reservation> reservations = reservationService.getReservationsByDate(date);
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for " + date);
        } else {
            System.out.println("\n--- Reservations for " + date + " ---");
            reservations.forEach(System.out::println);
        }
    }

    private void viewReservationsByCustomer() {
        int customerId = getIntInput("Enter customer ID: ");
        List<Reservation> reservations = reservationService.getReservationsByCustomer(customerId);
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this customer.");
        } else {
            System.out.println("\n--- Reservations for Customer ---");
            reservations.forEach(System.out::println);
        }
    }

    private void displayReports() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== REPORTS & ANALYTICS ===");
            System.out.println("1. Revenue Report");
            System.out.println("2. Customer Loyalty Report");
            System.out.println("3. Menu Popularity Report");
            System.out.println("4. Table Utilization Report");
            System.out.println("5. Daily Sales Summary");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> displayRevenueReport();
                case 2 -> displayLoyaltyReport();
                case 3 -> displayMenuPopularityReport();
                case 4 -> displayTableUtilizationReport();
                case 5 -> displayDailySalesSummary();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void displayRevenueReport() {
        List<Order> orders = orderService.getAllOrders();
        double totalRevenue = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        long pendingOrders = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .count();

        long preparingOrders = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PREPARING)
                .count();

        System.out.println("\n=== REVENUE REPORT ===");
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        System.out.println("Pending Orders: " + pendingOrders);
        System.out.println("Preparing Orders: " + preparingOrders);
        System.out.println("Total Orders: " + orders.size());
    }

    private void displayLoyaltyReport() {
        List<Customer> customers = customerService.getAllCustomers();

        System.out.println("\n=== CUSTOMER LOYALTY REPORT ===");
        customers.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getLoyaltyPoints(), c1.getLoyaltyPoints()))
                .limit(10)
                .forEach(customer ->
                        System.out.println(customer.getFullName() + ": " +
                                customer.getLoyaltyPoints() + " points"));
    }

    private void displayMenuPopularityReport() {
        List<Order> orders = orderService.getAllOrders();
        Map<MenuItem, Long> itemCount = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        System.out.println("\n=== MENU POPULARITY REPORT ===");
        itemCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .forEach(entry ->
                        System.out.println(entry.getKey().getName() +
                                ": " + entry.getValue() + " orders"));
    }

    private void displayTableUtilizationReport() {
        List<Table> tables = tableService.getAllTables();
        List<Reservation> reservations = reservationService.getAllReservations();

        System.out.println("\n=== TABLE UTILIZATION REPORT ===");
        for (Table table : tables) {
            long reservationCount = reservations.stream()
                    .filter(reservation -> reservation.getTable().equals(table))
                    .count();

            System.out.println("Table #" + table.getTableNumber() +
                    ": " + reservationCount + " reservations | Status: " + table.getStatus());
        }
    }

    private void displayDailySalesSummary() {
        List<Order> orders = orderService.getAllOrders();
        Map<LocalDate, Double> dailySales = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID)
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate(),
                        Collectors.summingDouble(Order::getTotalAmount)
                ));

        System.out.println("\n=== DAILY SALES SUMMARY ===");
        dailySales.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println(entry.getKey() + ": $" +
                                String.format("%.2f", entry.getValue())));
    }

    // Helper methods for input handling
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String getStringInputOrKeep(String prompt, String currentValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? currentValue : input;
    }

    private boolean getYesNoInput(String prompt) {
        while (true) {
            String input = getStringInput(prompt).toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            }
            System.out.println("Please enter 'yes' or 'no'.");
        }
    }

    private boolean getYesNoInputOrKeep(String prompt, boolean currentValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.isEmpty() || input.equals("keep")) {
            return currentValue;
        }
        return input.equals("yes") || input.equals("y");
    }

    private LocalDate getDateInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD: ");
            }
        }
    }

    private LocalTime getTimeInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return LocalTime.parse(input, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:MM: ");
            }
        }
    }

    private void initializeSampleData() {
        try {
            // Add sample categories
            MenuCategory appetizers = new MenuCategory(1, "Appetizers", "Starters and small plates");
            MenuCategory mainCourse = new MenuCategory(2, "Main Course", "Main dishes");
            MenuCategory desserts = new MenuCategory(3, "Desserts", "Sweet treats");
            MenuCategory beverages = new MenuCategory(4, "Beverages", "Drinks and cocktails");

            categoryService.addCategory(appetizers);
            categoryService.addCategory(mainCourse);
            categoryService.addCategory(desserts);
            categoryService.addCategory(beverages);

            // Add sample menu items
            menuItemService.addMenuItem(new MenuItem(1, "Garlic Bread", "Fresh bread with garlic butter", 5.99, appetizers, true));
            menuItemService.addMenuItem(new MenuItem(2, "Caesar Salad", "Fresh romaine with Caesar dressing", 8.99, appetizers, true));
            menuItemService.addMenuItem(new MenuItem(3, "Grilled Salmon", "Atlantic salmon with lemon butter", 22.99, mainCourse, true));
            menuItemService.addMenuItem(new MenuItem(4, "Ribeye Steak", "12oz ribeye with mashed potatoes", 28.99, mainCourse, true));
            menuItemService.addMenuItem(new MenuItem(5, "Chocolate Cake", "Rich chocolate layer cake", 7.99, desserts, true));
            menuItemService.addMenuItem(new MenuItem(6, "Tiramisu", "Classic Italian dessert", 8.99, desserts, true));
            menuItemService.addMenuItem(new MenuItem(7, "Coca Cola", "Regular coke", 2.99, beverages, true));
            menuItemService.addMenuItem(new MenuItem(8, "Orange Juice", "Fresh squeezed orange juice", 3.99, beverages, true));

            // Add sample staff
            staffService.addStaff(new Staff(1, "John Smith", "Manager", "john@restaurant.com", "555-0101"));
            staffService.addStaff(new Staff(2, "Sarah Johnson", "Chef", "sarah@restaurant.com", "555-0102"));
            staffService.addStaff(new Staff(3, "Mike Wilson", "Waiter", "mike@restaurant.com", "555-0103"));

            // Add sample customers
            customerService.addCustomer(new Customer(1, "Alice Brown", "alice@email.com", "555-0201"));
            customerService.addCustomer(new Customer(2, "Bob Davis", "bob@email.com", "555-0202"));
            customerService.addCustomer(new Customer(3, "Carol White", "carol@email.com", "555-0203"));

            // Add sample tables
            tableService.addTable(new Table(1, 1, 2, TableStatus.AVAILABLE));
            tableService.addTable(new Table(2, 2, 4, TableStatus.AVAILABLE));
            tableService.addTable(new Table(3, 3, 6, TableStatus.AVAILABLE));
            tableService.addTable(new Table(4, 4, 8, TableStatus.AVAILABLE));
            tableService.addTable(new Table(5, 5, 4, TableStatus.AVAILABLE));

        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }
}