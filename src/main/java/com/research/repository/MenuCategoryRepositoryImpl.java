package com.research.repository;

import com.research.model.MenuCategory;

public class MenuCategoryRepositoryImpl extends BaseRepository<MenuCategory> implements MenuCategoryRepository {

    @Override
    protected int getId(MenuCategory entity) {
        return entity.getId();
    }

    @Override
    protected void setId(MenuCategory entity, int id) {
        entity.setId(id);
    }
}