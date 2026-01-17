package com.research.model;

import java.util.Objects;

public class MenuCategory {
    private int id;
    private String name;
    private String description;
    private boolean available; // Add this field

    public MenuCategory() {
        this.available = true; // Default to available
    }

    public MenuCategory(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = true; // Default to available
    }

    public MenuCategory(int id, String name, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; } // Add this getter
    public void setAvailable(boolean available) { this.available = available; } // Add this setter

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuCategory that = (MenuCategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Description: %s, Available: %s",
                id, name, description, available ? "Yes" : "No");
    }
}