package database;

import lombok.Getter;
import models.Model;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class DatabaseTest {
    protected abstract Database<TestModel> getDatabase();

    @Test
    public void databaseIntegration() {
        Database<TestModel> database = getDatabase();
        assertNotNull(database);
        TestModel testModel = new TestModel(1L, "Test Data");
        TestModel testModel2 = new TestModel(2L, "Test Data 2");
        assertEquals(database.getAll().size(), 0);

        Optional<TestModel> receivedModel = database.create(testModel);
        assertTrue(receivedModel.isPresent());
        assertEquals(receivedModel.get(), testModel);
        receivedModel = database.create(testModel);
        assertTrue(receivedModel.isEmpty());

        assertEquals(database.getAll().size(), 1);
        assertTrue(database.getById(1L).isPresent());
        assertEquals(database.getById(1L).get(), testModel);

        receivedModel = database.create(testModel2);
        assertTrue(receivedModel.isPresent());
        assertEquals(database.getAll().size(), 2);

        TestModel testModel2Updated = new TestModel(2L, "Test Data Updated");
        receivedModel = database.update(testModel2Updated);
        assertTrue(receivedModel.isPresent());
        assertEquals(receivedModel.get(), testModel2);
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(database.getById(2L).get(), testModel2Updated);
        assertEquals(database.getAll().size(), 2);

        database.deleteById(3L);
        assertEquals(database.getAll().size(), 2);

        database.deleteById(1L);
        assertEquals(database.getAll().size(), 1);
        assertTrue(database.getById(1L).isEmpty());

        receivedModel = database.update(testModel);
        assertTrue(receivedModel.isEmpty());
        assertEquals(database.getAll().size(), 2);
        assertEquals(database.getById(1L).get(), testModel);
    }

    @Getter
    protected static class TestModel implements Model {
        protected final Long id;
        protected final String data;

        protected TestModel(Long id, String data) {
            this.id = id;
            this.data = data;
        }

        @Override
        public Long getId() {
            return this.id;
        }
    }
}
