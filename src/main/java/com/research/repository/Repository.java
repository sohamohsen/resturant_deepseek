package com.research.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void add(T entity);
    void update(T entity);
    void delete(int id);
    Optional<T> findById(int id);
    List<T> findAll();
    boolean existsById(int id);
}