package com.research.service;

import com.research.exception.NotFoundException;
import com.research.exception.ValidationException;
import com.research.model.Customer;
import com.research.model.Reservation;
import com.research.model.Table;
import com.research.model.TableStatus;
import com.research.repository.CustomerRepository;
import com.research.repository.ReservationRepository;
import com.research.repository.TableRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final TableService tableService;
    private final ValidationService validationService;

    public ReservationService(ReservationRepository reservationRepository,
                              CustomerRepository customerRepository,
                              TableRepository tableRepository,
                              TableService tableService) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.tableRepository = tableRepository;
        this.tableService = tableService;
        this.validationService = new ValidationService();
    }

    public Reservation createReservation(int customerId, int tableId,
                                         LocalDate date, LocalTime time,
                                         int numberOfGuests) {
        validateReservationCreation(customerId, tableId, date, time, numberOfGuests);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer with ID " + customerId + " not found"));

        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException("Table with ID " + tableId + " not found"));

        // Check table availability
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new ValidationException("Table #" + table.getTableNumber() + " is not available");
        }

        // Check capacity
        validationService.validateCapacity(table.getCapacity(), numberOfGuests);

        // Check for overlapping reservations
        if (reservationRepository.hasOverlappingReservation(table, date, time)) {
            throw new ValidationException("Table #" + table.getTableNumber() +
                    " already has a reservation at " + time);
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setTable(table);
        reservation.setReservationDate(date);
        reservation.setReservationTime(time);
        reservation.setNumberOfGuests(numberOfGuests);

        reservationRepository.add(reservation);

        // Update table status
        tableService.updateTableStatus(tableId, TableStatus.RESERVED);

        return reservation;
    }

    public void cancelReservation(int reservationId) {
        Reservation reservation = getReservationById(reservationId);

        // Update table status back to available
        tableService.updateTableStatus(reservation.getTable().getId(), TableStatus.AVAILABLE);

        reservationRepository.delete(reservationId);
    }

    public Reservation getReservationById(int id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation with ID " + id + " not found"));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationRepository.findByCustomer(customerId);
    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByDate(date);
    }

    public boolean checkTableAvailability(int tableId, LocalDate date, LocalTime time) {
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException("Table with ID " + tableId + " not found"));

        if (table.getStatus() != TableStatus.AVAILABLE) {
            return false;
        }

        return !reservationRepository.hasOverlappingReservation(table, date, time);
    }

    public List<Table> getAvailableTablesForReservation(LocalDate date, LocalTime time, int numberOfGuests) {
        List<Table> availableTables = tableService.getAvailableTables();

        return availableTables.stream()
                .filter(table -> table.getCapacity() >= numberOfGuests)
                .filter(table -> !reservationRepository.hasOverlappingReservation(table, date, time))
                .toList();
    }

    private void validateReservationCreation(int customerId, int tableId,
                                             LocalDate date, LocalTime time,
                                             int numberOfGuests) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer with ID " + customerId + " not found");
        }

        if (!tableRepository.existsById(tableId)) {
            throw new NotFoundException("Table with ID " + tableId + " not found");
        }

        validationService.validatePositive(numberOfGuests, "Number of guests");

        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException("Reservation date cannot be in the past");
        }

        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new ValidationException("Reservation time cannot be in the past");
        }
    }
}