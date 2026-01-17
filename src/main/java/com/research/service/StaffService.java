package com.research.service;

import com.research.exception.NotFoundException;
import com.research.model.Staff;
import com.research.repository.StaffRepository;

import java.util.List;

public class StaffService {
    private final StaffRepository staffRepository;
    private final ValidationService validationService;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
        this.validationService = new ValidationService();
    }

    public void addStaff(Staff staff) {
        validateStaff(staff);
        staffRepository.add(staff);
    }

    public void updateStaff(Staff staff) {
        if (!staffRepository.existsById(staff.getId())) {
            throw new NotFoundException("Staff with ID " + staff.getId() + " not found");
        }
        validateStaff(staff);
        staffRepository.update(staff);
    }

    public void deleteStaff(int id) {
        staffRepository.delete(id);
    }

    public Staff getStaffById(int id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff with ID " + id + " not found"));
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public List<Staff> searchStaffByName(String name) {
        return staffRepository.findByName(name);
    }

    public List<Staff> searchStaffByRole(String role) {
        return staffRepository.findByRole(role);
    }

    private void validateStaff(Staff staff) {
        validationService.validateString(staff.getFullName(), "Full name");
        validationService.validateString(staff.getRole(), "Role");
        validationService.validateEmail(staff.getEmail());
        validationService.validatePhone(staff.getPhone());
    }
}