package com.research.service;

import com.research.exception.BusinessRuleException;
import com.research.exception.ValidationException;
import com.research.model.MenuItem;
import com.research.repository.MenuCategoryRepository;
import com.research.repository.MenuItemRepository;

public class MenuService {
    private final MenuCategoryRepository categoryRepository;
    private final MenuItemRepository itemRepository;

    // Use dependency injection in constructor
    public MenuService(MenuCategoryRepository categoryRepository,
                       MenuItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public void addMenuItem(MenuItem item) {
        if (item.getPrice() <= 0) {
            throw new ValidationException("Price must be positive");
        }
        if (!item.getCategory().isAvailable()) {
            throw new BusinessRuleException("Cannot add item to unavailable category");
        }
        itemRepository.add(item);
    }

    // Other menu-related methods
}