package com.research.service;

import com.research.exception.NotFoundException;
import com.research.exception.ValidationException;
import com.research.model.Table;
import com.research.model.TableStatus;
import com.research.repository.TableRepository;

import java.util.List;

public class TableService {
    private final TableRepository tableRepository;
    private final ValidationService validationService;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
        this.validationService = new ValidationService();
    }

    public void addTable(Table table) {
        validateTable(table);
        if (tableRepository.findByTableNumber(table.getTableNumber()).isPresent()) {
            throw new ValidationException("Table number " + table.getTableNumber() + " already exists");
        }
        tableRepository.add(table);
    }

    public void updateTable(Table table) {
        if (!tableRepository.existsById(table.getId())) {
            throw new NotFoundException("Table with ID " + table.getId() + " not found");
        }
        validateTable(table);
        tableRepository.update(table);
    }

    public void deleteTable(int id) {
        tableRepository.delete(id);
    }

    public Table getTableById(int id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Table with ID " + id + " not found"));
    }

    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    public List<Table> getAvailableTables() {
        return tableRepository.findByStatus(TableStatus.AVAILABLE);
    }

    public List<Table> getTablesByCapacity(int minCapacity) {
        validationService.validatePositive(minCapacity, "Minimum capacity");
        return tableRepository.findByCapacity(minCapacity);
    }

    public void updateTableStatus(int tableId, TableStatus status) {
        Table table = getTableById(tableId);
        table.setStatus(status);
        tableRepository.update(table);
    }

    public boolean isTableAvailable(int tableId) {
        Table table = getTableById(tableId);
        return table.getStatus() == TableStatus.AVAILABLE;
    }

    private void validateTable(Table table) {
        validationService.validatePositive(table.getTableNumber(), "Table number");
        validationService.validatePositive(table.getCapacity(), "Table capacity");

        if (table.getCapacity() > 20) {
            throw new ValidationException("Table capacity cannot exceed 20");
        }
    }
}