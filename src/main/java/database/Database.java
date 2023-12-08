package database;

import models.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Database<T extends Model> {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    /**
     * returns the Model with the same id.
     */
    public abstract Optional<T> getById(Long id);

    /**
     * returns all entries of the model.
     */
    public abstract List<T> getAll();

    /**
     * creates and returns a model. null on failure.
     */
    public final Optional<T> create(T model) {
        Optional<T> optional = createImplementation(model);
        if (optional.isPresent()) changes.firePropertyChange(Property.CREATE.name, null, model);
        return optional;
    }

    /**
     * updates the model with the same id in the database. returns the previous entry. empty if not present.
     * If the entry does not exist a new one will be created.
     */
    public final Optional<T> update(T model) {
        Optional<T> optional = updateImplementation(model);
        if (optional.isPresent()) changes.firePropertyChange(Property.UPDATE.name, getById(model.getId()).orElse(null), model);
        return optional;
    }

    /**
     * Deletes the entry from the database.
     */
    public final void deleteById(Long id) {
        deleteByIdImplementation(id);
        changes.firePropertyChange(Property.DELETE.name, getById(id).orElse(null), null);
    }

    protected abstract Optional<T> updateImplementation(T model);
    protected abstract void deleteByIdImplementation(Long id);

    protected abstract Optional<T> createImplementation(T model);

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}
