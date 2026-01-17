package com.research.repository;

import com.research.model.Reservation;
import com.research.model.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends Repository<Reservation> {
    List<Reservation> findByCustomer(int customerId);
    List<Reservation> findByDate(LocalDate date);
    boolean hasOverlappingReservation(Table table, LocalDate date, LocalTime time);
}