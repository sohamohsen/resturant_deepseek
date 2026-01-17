package com.research.repository;

import com.research.model.MenuItem;

import java.util.List;

public interface MenuItemRepository extends Repository<MenuItem> {
    List<MenuItem> findByName(String name);
    List<MenuItem> findByCategory(int categoryId);
    List<MenuItem> findAvailableItems();
}