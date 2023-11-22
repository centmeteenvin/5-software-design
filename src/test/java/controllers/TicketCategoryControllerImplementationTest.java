package controllers;

import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TicketCategoryControllerImplementationTest {
    private Database<Ticket> mockTicketDatabase;
    private Database<TicketCategory> mockTicketCategoryDatabase;

    private TicketController mockTicketController;
    private TicketCategoryControllerImplementation controller;

    @BeforeEach
    void setup() {
        mockTicketDatabase = (Database<Ticket>) mock(Database.class);
        mockTicketCategoryDatabase = (Database<TicketCategory>) mock(Database.class);

        mockTicketController = mock(TicketController.class);

        controller = new TicketCategoryControllerImplementation(mockTicketCategoryDatabase, mockTicketDatabase);
        controller.setTicketController(mockTicketController);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).create(any());

        Optional<TicketCategory> receivedCategory = controller.create("foo");

        verify(mockTicketCategoryDatabase, times(1)).create(any());
        assertEquals(receivedCategory, Optional.empty());

        TicketCategory testCategory = new TicketCategory(1L, "foo");
        doReturn(Optional.of(testCategory)).when(mockTicketCategoryDatabase).create(any());

        receivedCategory = controller.create("foo");

        verify(mockTicketCategoryDatabase, times(2)).create(any());
        assertEquals(receivedCategory.get(), testCategory);
    }

    @Test
    void addTicket() throws TicketNotFoundException, CategoryNotFoundException {
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).update(any());

        assertThrows(CategoryNotFoundException.class,() -> controller.addTicket(1L, 1L));

        verify(mockTicketCategoryDatabase, times(1)).getById(any());
        verify(mockTicketDatabase, never()).getById(any());
        verify(mockTicketCategoryDatabase, never()).update(any());

        TicketCategory testTicketCategory = spy(new TicketCategory(1L, "foo"));
        doReturn(Optional.of(testTicketCategory)).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());

        assertThrows(TicketNotFoundException.class,() -> controller.addTicket(1L, 1L));

        verify(mockTicketCategoryDatabase, times(2)).getById(any());
        verify(mockTicketDatabase, times(1)).getById(any());
        verify(mockTicketCategoryDatabase, never()).update(any());
        assertTrue(testTicketCategory.getTicketIds().isEmpty());

        Ticket testTicket = new Ticket(1L, 0, 0L);
        doReturn(Optional.of(testTicketCategory)).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.of(testTicket)).when(mockTicketDatabase).getById(any());

        controller.addTicket(1L, 1L);

        verify(mockTicketCategoryDatabase, times(3)).getById(any());
        verify(mockTicketDatabase, times(2)).getById(any());
        verify(mockTicketCategoryDatabase, times(1)).update(any());
        assertEquals(testTicketCategory.getTicketIds().size(), 1);

        verifyNoInteractions(mockTicketController);
    }

    @Test
    void removeTicket() throws TicketNotFoundException, CategoryNotFoundException {
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).update(any());

        assertThrows(CategoryNotFoundException.class,() -> controller.removeTicket(1L, 1L));

        verify(mockTicketCategoryDatabase, times(1)).getById(any());
        verify(mockTicketDatabase, never()).getById(any());
        verify(mockTicketCategoryDatabase, never()).update(any());

        TicketCategory testTicketCategory = spy(new TicketCategory(1L, "foo"));
        testTicketCategory.getTicketIds().add(1L);
        doReturn(Optional.of(testTicketCategory)).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());

        assertThrows(TicketNotFoundException.class,() -> controller.removeTicket(1L, 1L));

        verify(mockTicketCategoryDatabase, times(2)).getById(any());
        verify(mockTicketDatabase, times(1)).getById(any());
        verify(mockTicketCategoryDatabase, never()).update(any());
        assertEquals(testTicketCategory.getTicketIds().size(), 1);

        Ticket testTicket = new Ticket(1L, 0, 1L);
        doReturn(Optional.of(testTicketCategory)).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.of(testTicket)).when(mockTicketDatabase).getById(any());

        controller.removeTicket(1L, 2L);

        verify(mockTicketCategoryDatabase, times(3)).getById(any());
        verify(mockTicketDatabase, times(2)).getById(any());
        verify(mockTicketCategoryDatabase, never()).update(any());
        assertEquals(testTicketCategory.getTicketIds().size(), 1);

        controller.removeTicket(1L, 1L);

        verify(mockTicketCategoryDatabase, times(4)).getById(any());
        verify(mockTicketDatabase, times(3)).getById(any());
        verify(mockTicketCategoryDatabase, times(1)).update(any());
        assertTrue(testTicketCategory.getTicketIds().isEmpty());

        verifyNoInteractions(mockTicketController);
    }

    @Test
    void rename() throws CategoryNotFoundException {
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).update(any());

        TicketCategory testCategory = spy(new TicketCategory(1L, "foo"));

        assertThrows(CategoryNotFoundException.class, () -> controller.rename(1L, "faa"));

        assertEquals(testCategory.getName(), "foo");
        verify(mockTicketCategoryDatabase, times(1)).getById(any());
        verify(mockTicketCategoryDatabase, never()).update(any());

        doReturn(Optional.of(testCategory)).when(mockTicketCategoryDatabase).getById(any());

        controller.rename(1L, "faa");

        assertEquals(testCategory.getName(), "faa");
        verify(mockTicketCategoryDatabase, times(2)).getById(any());
        verify(mockTicketCategoryDatabase, times(1)).update(any());
    }

    @Test
    void delete() throws TicketNotFoundException, CategoryNotFoundException {
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).getById(any());
        doNothing().when(mockTicketController).changeCategory(any(),any());
        doNothing().when(mockTicketCategoryDatabase).deleteById(any());

        assertThrows(CategoryNotFoundException.class, () -> controller.delete(1L));

        verify(mockTicketCategoryDatabase,times(1)).getById(any());
        verify(mockTicketController, never()).changeCategory(any(),any());
        verify(mockTicketCategoryDatabase,never()).deleteById(any());

        TicketCategory testCategory = new TicketCategory(1L, "foo");
        doReturn(Optional.of(testCategory)).when(mockTicketCategoryDatabase).getById(any());

        controller.delete(1L);

        verify(mockTicketCategoryDatabase,times(2)).getById(any());
        verify(mockTicketController, never()).changeCategory(any(),any());
        verify(mockTicketCategoryDatabase,times(1)).deleteById(any());

        testCategory.getTicketIds().add(1L);
        testCategory.getTicketIds().add(2L);

        controller.delete(1L);

        verify(mockTicketCategoryDatabase,times(3)).getById(any());
        verify(mockTicketController, times(2)).changeCategory(any(),any());
        verify(mockTicketCategoryDatabase,times(2)).deleteById(any());
    }
}
