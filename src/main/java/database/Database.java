package database;

import models.Model;

import java.util.Optional;

public interface Database {
    Optional<Model> getById(Long id);
    Optional<Model> create(Model model);
    Optional<Model> update(Model model);
    void deleteById(Long id);
}
