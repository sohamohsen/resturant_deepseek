package com.research.service;

import com.research.exception.NotFoundException;
import com.research.model.MenuCategory;
import com.research.repository.MenuCategoryRepository;

import java.util.List;

public class MenuCategoryService {
    private final MenuCategoryRepository categoryRepository;
    private final ValidationService validationService;

    public MenuCategoryService(MenuCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.validationService = new ValidationService();
    }

    public void addCategory(MenuCategory category) {
        validateCategory(category);
        categoryRepository.add(category);
    }

    public void updateCategory(MenuCategory category) {
        if (!categoryRepository.existsById(category.getId())) {
            throw new NotFoundException("Category with ID " + category.getId() + " not found");
        }
        validateCategory(category);
        categoryRepository.update(category);
    }

    public void deleteCategory(int id) {
        categoryRepository.delete(id);
    }

    public MenuCategory getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    public List<MenuCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    private void validateCategory(MenuCategory category) {
        validationService.validateString(category.getName(), "Category name");
        validationService.validateString(category.getDescription(), "Category description");
    }
}