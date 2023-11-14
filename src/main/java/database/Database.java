package database;

import models.Model;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Database<T extends Model> {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public abstract Optional<T> getById(Long id);
    public final Optional<T> create(T model) {
        changes.firePropertyChange("Model Creation", null, model);
        return createImplementation(model);
    }

    public final Optional<T> update(T model) {
        changes.firePropertyChange("Model Modification", getById(model.getId()).get(), model);
        return updateImplementation(model);
    }

    public final void deleteById(Long id) {
        changes.firePropertyChange("Model Deletion", getById(id).get(), null);
        deleteByIdImplementation(id);
    }

    protected abstract Optional<T> updateImplementation(T model);
    protected abstract void deleteByIdImplementation(Long id);

    protected abstract Optional<T> createImplementation(T Model);
}
