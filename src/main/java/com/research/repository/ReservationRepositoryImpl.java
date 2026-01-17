package com.research.repository;

import com.research.model.Reservation;
import com.research.model.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryImpl extends BaseRepository<Reservation> implements ReservationRepository {

    @Override
    protected int getId(Reservation entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Reservation entity, int id) {
        entity.setId(id);
    }

    @Override
    public List<Reservation> findByCustomer(int customerId) {
        return entities.values().stream()
                .filter(reservation -> reservation.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByDate(LocalDate date) {
        return entities.values().stream()
                .filter(reservation -> reservation.getReservationDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasOverlappingReservation(Table table, LocalDate date, LocalTime time) {
        return entities.values().stream()
                .anyMatch(reservation ->
                        reservation.getTable().equals(table) &&
                                reservation.getReservationDate().equals(date) &&
                                Math.abs(reservation.getReservationTime().getHour() - time.getHour()) < 2
                );
    }
}