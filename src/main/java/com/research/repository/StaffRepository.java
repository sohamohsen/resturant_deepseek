package com.research.repository;

import com.research.model.Staff;

import java.util.List;

public interface StaffRepository extends Repository<Staff> {
    List<Staff> findByName(String name);
    List<Staff> findByRole(String role);
}