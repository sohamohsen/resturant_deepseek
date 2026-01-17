package com.research.repository;

import com.research.model.Staff;
import java.util.List;
import java.util.stream.Collectors;

public class StaffRepositoryImpl extends BaseRepository<Staff> implements StaffRepository {

    @Override
    protected int getId(Staff entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Staff entity, int id) {
        entity.setId(id);
    }

    @Override
    public List<Staff> findByName(String name) {
        return entities.values().stream()
                .filter(staff -> staff.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Staff> findByRole(String role) {
        return entities.values().stream()
                .filter(staff -> staff.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }
}