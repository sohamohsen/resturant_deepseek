package com.research.repository;

import com.research.model.Table;
import com.research.model.TableStatus;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends Repository<Table> {
    List<Table> findByStatus(TableStatus status);
    List<Table> findByCapacity(int minCapacity);
    Optional<Table> findByTableNumber(int tableNumber);
}