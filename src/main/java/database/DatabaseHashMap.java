package database;

import lombok.Getter;
import models.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatabaseHashMap<T extends Model> extends Database<T> {

    @Getter
    private final Map<Long, T> data = new HashMap<>();
    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<T> getAll() {
        return data.values().stream().toList();
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
    protected Optional<T> createImplementation(T model) {
        if (data.containsKey(model.getId())) {
            return Optional.empty();
        }
        data.put(model.getId(), model);
        return Optional.of(model);
    }
}
