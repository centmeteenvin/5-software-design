package database;

import lombok.Getter;
import models.Model;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseHashMapTest {

    @Test
    void getById() {
        final DatabaseHashMap<TestModel> databaseHashMap = new DatabaseHashMap<>();
        TestModel testModel = new TestModel(1L, "Test Data");
        TestModel testModel2 = new TestModel(2L, "Test Data 2");
        databaseHashMap.getData().put(1L, testModel);
        databaseHashMap.getData().put(2L, testModel2);

        assertTrue(databaseHashMap.getById(3L).isEmpty());
        assertTrue(databaseHashMap.getById(1L).isPresent());
        assertTrue(databaseHashMap.getById(2L).isPresent());

        assertEquals(databaseHashMap.getById(1L).get(), testModel);
        assertEquals(databaseHashMap.getById(2L).get(), testModel2);

    }

    @Test
    void getAll() {
        final DatabaseHashMap<TestModel> databaseHashMap = new DatabaseHashMap<>();
        TestModel testModel = new TestModel(1L, "Test Data");
        TestModel testModel2 = new TestModel(2L, "Test Data 2");
        databaseHashMap.getData().put(1L, testModel);
        databaseHashMap.getData().put(2L, testModel2);

        assertFalse(databaseHashMap.getAll().isEmpty());
        assertEquals(databaseHashMap.getAll().size(), 2);
        assertEquals(databaseHashMap.getAll(), List.of(testModel, testModel2));
    }

    @Test
    void updateImplementation() {
        final DatabaseHashMap<TestModel> databaseHashMap = new DatabaseHashMap<>();
        TestModel testModel = new TestModel(1L, "Test Data");
        TestModel testModel2 = new TestModel(1L, "Test Data 2");

        Optional<TestModel> receivedModel = databaseHashMap.update(testModel);
        assertTrue(receivedModel.isEmpty());
        assertEquals(databaseHashMap.getData().get(testModel.id), testModel);

        receivedModel = databaseHashMap.update(testModel2);
        assertTrue(receivedModel.isPresent());
        assertEquals(receivedModel.get(), testModel);
        assertEquals(databaseHashMap.getData().get(testModel.id), testModel2);
    }

    @Test
    void deleteByIdImplementation() {
        final DatabaseHashMap<TestModel> databaseHashMap = new DatabaseHashMap<>();
        TestModel testModel = new TestModel(1L, "Test Data");
        TestModel testModel2 = new TestModel(2L, "Test Data 2");
        databaseHashMap.getData().put(1L, testModel);
        databaseHashMap.getData().put(2L, testModel2);

        databaseHashMap.deleteById(testModel.id);
        assertEquals(databaseHashMap.getData().size(), 1);

        databaseHashMap.deleteById(testModel.id);
        assertEquals(databaseHashMap.getData().size(), 1);

        databaseHashMap.deleteById(testModel2.id);
        assertEquals(databaseHashMap.getData().size(), 0);
    }

    @Test
    void createImplementation() {
        DatabaseHashMap<TestModel> databaseHashMap = new DatabaseHashMap<>();
        TestModel testModel = new TestModel(1L, "Test Data");
        Optional<TestModel> receivedModel = databaseHashMap.create(testModel);
        assertTrue(receivedModel.isPresent());
        assertEquals(receivedModel.get(), testModel);
        assertTrue(databaseHashMap.getData().containsKey(1L));
        assertEquals(databaseHashMap.getData().get(1L), testModel);

        TestModel testModel2 = new TestModel(1L, "Test Data 2");
        receivedModel = databaseHashMap.create(testModel2);
        assertTrue(receivedModel.isEmpty());
        assertEquals(databaseHashMap.getData().get(1L).getData(), "Test Data");
    }

    @Getter
    private static class TestModel implements Model {
        private final Long id;
        private final String data;

        private TestModel(Long id, String data) {
            this.id = id;
            this.data = data;
        }

        @Override
        public Long getId() {
            return this.id;
        }
    }
}