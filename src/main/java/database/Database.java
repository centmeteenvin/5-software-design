package database;

import models.Model;

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
        changes.firePropertyChange("Model Creation", null, model);
        return createImplementation(model);
    }

    /**
     * updates the model with the same id in the database. returns the previous entry. null if not present.
     * If the entry does not exist a new one will be created.
     */
    public final Optional<T> update(T model) {
        changes.firePropertyChange("Model Modification", getById(model.getId()).get(), model);
        return updateImplementation(model);
    }

    /**
     * Deletes the entry from the database.
     */
    public final void deleteById(Long id) {
        changes.firePropertyChange("Model Deletion", getById(id).get(), null);
        deleteByIdImplementation(id);
    }

    protected abstract Optional<T> updateImplementation(T model);
    protected abstract void deleteByIdImplementation(Long id);

    protected abstract Optional<T> createImplementation(T Model);
}
