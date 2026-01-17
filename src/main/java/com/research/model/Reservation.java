package com.research.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int id;
    private Customer customer;
    private Table table;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numberOfGuests;

    // Add no-argument constructor
    public Reservation() {
        // Initialize with default values
        this.reservationDate = LocalDate.now();
        this.reservationTime = LocalTime.now();
    }

    // Keep existing constructor
    public Reservation(int id, Customer customer, Table table,
                       LocalDate reservationDate, LocalTime reservationTime,
                       int numberOfGuests) {
        this.id = id;
        this.customer = customer;
        this.table = table;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.numberOfGuests = numberOfGuests;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public LocalTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalTime reservationTime) { this.reservationTime = reservationTime; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    @Override
    public String toString() {
        return String.format("Reservation{id=%d, customer='%s', table=%d, date=%s, time=%s, guests=%d}",
                id, customer.getFullName(), table.getTableNumber(),
                reservationDate, reservationTime, numberOfGuests);
    }
}