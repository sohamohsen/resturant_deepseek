package com.research.repository;

import com.research.model.MenuItem;
import java.util.List;
import java.util.stream.Collectors;

public class MenuItemRepositoryImpl extends BaseRepository<MenuItem> implements MenuItemRepository {

    @Override
    protected int getId(MenuItem entity) {
        return entity.getId();
    }

    @Override
    protected void setId(MenuItem entity, int id) {
        entity.setId(id);
    }

    @Override
    public List<MenuItem> findByName(String name) {
        return entities.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findByCategory(int categoryId) {
        return entities.values().stream()
                .filter(item -> item.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findAvailableItems() {
        return entities.values().stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }
}