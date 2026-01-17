package com.research.repository;

import com.research.exception.DuplicateIdException;
import com.research.exception.NotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseRepository<T> implements Repository<T> {
    protected final Map<Integer, T> entities = new HashMap<>();
    private int nextId = 1;

    @Override
    public void add(T entity) {
        int id = getId(entity);
        if (id != 0 && entities.containsKey(id)) {
            throw new DuplicateIdException("Entity with ID " + id + " already exists");
        }
        if (id == 0) {
            id = getAndIncrementNextId();
            setId(entity, id);
        } else if (id >= nextId) {
            nextId = id + 1;
        }
        entities.put(id, entity);
    }

    @Override
    public void update(T entity) {
        int id = getId(entity);
        if (!entities.containsKey(id)) {
            throw new NotFoundException("Entity with ID " + id + " not found");
        }
        entities.put(id, entity);
    }

    @Override
    public void delete(int id) {
        if (!entities.containsKey(id)) {
            throw new NotFoundException("Entity with ID " + id + " not found");
        }
        entities.remove(id);
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public boolean existsById(int id) {
        return entities.containsKey(id);
    }

    // Protected methods for subclasses to access ID generation
    protected synchronized int getAndIncrementNextId() {
        return nextId++;
    }

    protected int getCurrentNextId() {
        return nextId;
    }

    // Abstract methods for subclasses to implement
    protected abstract int getId(T entity);
    protected abstract void setId(T entity, int id);
}