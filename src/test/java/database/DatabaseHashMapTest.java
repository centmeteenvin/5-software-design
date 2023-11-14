package database;

import lombok.Getter;
import models.Model;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseHashMapTest {

    @Test
    void getById() {

    }

    @Test
    void getAll() {
    }

    @Test
    void updateImplementation() {
    }

    @Test
    void deleteByIdImplementation() {
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