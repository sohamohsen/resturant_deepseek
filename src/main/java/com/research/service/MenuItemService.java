package com.research.service;

import com.research.exception.NotFoundException;
import com.research.exception.ValidationException;
import com.research.model.MenuItem;
import com.research.repository.MenuCategoryRepository;
import com.research.repository.MenuItemRepository;

import java.util.List;

public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository categoryRepository;
    private final ValidationService validationService;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           MenuCategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.validationService = new ValidationService();
    }

    public void addMenuItem(MenuItem menuItem) {
        validateMenuItem(menuItem);
        validateCategoryExists(menuItem.getCategory().getId());
        menuItemRepository.add(menuItem);
    }

    public void updateMenuItem(MenuItem menuItem) {
        if (!menuItemRepository.existsById(menuItem.getId())) {
            throw new NotFoundException("Menu item with ID " + menuItem.getId() + " not found");
        }
        validateMenuItem(menuItem);
        validateCategoryExists(menuItem.getCategory().getId());
        menuItemRepository.update(menuItem);
    }

    public void deleteMenuItem(int id) {
        menuItemRepository.delete(id);
    }

    public MenuItem getMenuItemById(int id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu item with ID " + id + " not found"));
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> getAvailableItems() {
        return menuItemRepository.findAvailableItems();
    }

    public List<MenuItem> searchMenuItemsByName(String name) {
        return menuItemRepository.findByName(name);
    }

    public List<MenuItem> getItemsByCategory(int categoryId) {
        return menuItemRepository.findByCategory(categoryId);
    }

    private void validateMenuItem(MenuItem menuItem) {
        validationService.validateString(menuItem.getName(), "Item name");
        validationService.validateString(menuItem.getDescription(), "Item description");
        validationService.validatePositive(menuItem.getPrice(), "Price");

        if (menuItem.getCategory() == null) {
            throw new ValidationException("Menu item must have a category");
        }
    }

    private void validateCategoryExists(int categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category with ID " + categoryId + " not found");
        }
    }
}