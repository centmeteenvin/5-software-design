package database;

import models.Model;

import java.util.List;
import java.util.Optional;

public class DatabaseHashMap<T extends Model> extends Database<T> {
    @Override
    public Optional<T> getById(Long id) {
        return Optional.empty(); //TODO
    }

    @Override
    public List<T> getAll() {
        return null; //TODO
    }

    @Override
    protected Optional<T> updateImplementation(T model) {
        return Optional.empty(); //TODO
    }

    @Override
    protected void deleteByIdImplementation(Long id) {
        //TODO
    }

    @Override
    protected Optional<T> createImplementation(T Model) {
        return Optional.empty(); //TODO
    }
}
