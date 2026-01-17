package com.research.repository;

import com.research.model.Table;
import com.research.model.TableStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TableRepositoryImpl extends BaseRepository<Table> implements TableRepository {

    @Override
    protected int getId(Table entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Table entity, int id) {
        entity.setId(id);
    }

    @Override
    public List<Table> findByStatus(TableStatus status) {
        return entities.values().stream()
                .filter(table -> table.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Table> findByCapacity(int minCapacity) {
        return entities.values().stream()
                .filter(table -> table.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Table> findByTableNumber(int tableNumber) {
        return entities.values().stream()
                .filter(table -> table.getTableNumber() == tableNumber)
                .findFirst();
    }
}