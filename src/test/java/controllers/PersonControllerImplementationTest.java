package controllers;

import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.PersonNotFoundException;
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

public class PersonControllerImplementationTest {
    private Database<Person> mockPersonDatabase;
    private Database<Ticket> mockTicketDatabase;
    private Database<TicketCategory> mockTicketCategoryDatabase;

    private TicketController mockTicketController;
    private PersonControllerImplementation controller;
    private TicketCategoryController mockTicketCategoryController;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setup() {
        mockTicketDatabase = (Database<Ticket>) mock(Database.class);
        mockPersonDatabase = (Database<Person>) mock(Database.class);
        mockTicketCategoryDatabase = (Database<TicketCategory>) mock(Database.class);

        mockTicketController = mock(TicketController.class);
        mockTicketCategoryController = mock(TicketCategoryController.class);

        controller = new PersonControllerImplementation(mockPersonDatabase, mockTicketDatabase);
        controller.setTicketController(mockTicketController);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());

        Optional<Person> receivedPerson = controller.create("foo");
        assertTrue(receivedPerson.isEmpty());

        Person person = new Person(1L, "foo1");
        doReturn(Optional.of(person)).when(mockPersonDatabase).create(any());
        receivedPerson = controller.create("foo1");
        assertTrue(receivedPerson.isPresent());
        assertEquals(receivedPerson.get(), person);
    }

    @Test
    void addTicket() throws TicketNotFoundException, PersonNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());

        Person testPerson = new Person(1L, "foo");
        Ticket testTicket = new Ticket(2L, 100, 1L);
        assertThrows(PersonNotFoundException.class , () -> controller.addTicket(testPerson.getId(), 2L));



        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());
        assertThrows(TicketNotFoundException.class , () -> controller.addTicket(testPerson.getId(), 2L));


        doReturn(Optional.of(testTicket)).when(mockTicketDatabase).getById(any());
        controller.addTicket(testPerson.getId(), 2L);

        assertEquals(testPerson.getTicketsId().size(), 1);
        assertEquals(testPerson.getTicketsId().get(0), 2L);

        // Make sure ticketController is not used, otherwise cyclic reference
        verifyNoInteractions(mockTicketController);
    }

    @Test
    void removeTicket() throws PersonNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockPersonDatabase).update(any());

        Person testPerson = new Person(1L, "foo");
        testPerson.getTicketsId().add(1L);

        assertThrows(PersonNotFoundException.class ,() -> controller.removeTicket(testPerson.getId(), 1L));

        assertEquals(testPerson.getTicketsId().get(0), 1L);
        verify(mockPersonDatabase, never()).update(any());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());

        controller.removeTicket(testPerson.getId(), 1L);

        verify(mockPersonDatabase, times(1)).update(any());
        assertTrue(testPerson.getTicketsId().isEmpty());

        // Make sure ticketController is not used, otherwise cyclic reference
        verifyNoInteractions(mockTicketController);
    }

    @Test
    void Delete() throws PersonNotFoundException, TicketNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doNothing().when(mockPersonDatabase).deleteById(any());
        doNothing().when(mockTicketController).removePerson(any(), any());

        Person testPerson = spy(new Person(1L, "foo"));

        // Do nothing (Check)
        assertThrows(PersonNotFoundException.class , () ->controller.delete(1L));

        verify(mockPersonDatabase, never()).deleteById(any());
        verify(mockTicketController, never()).removePerson(any(), any());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(1L);

        // Only run "deleteById" once (Check)
        controller.delete(1L);

        verify(mockPersonDatabase, times(1)).deleteById(any());
        verify(mockTicketController, never()).removePerson(any(), any());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(1L);

        testPerson.getTicketsId().add(1L);
        testPerson.getTicketsId().add(2L);

        // Run "deleteById" once and "removePerson" twice
        controller.delete(1L);

        verify(mockPersonDatabase, times(2)).deleteById(any());
        verify(mockTicketController, times(1)).removePerson(1L, 1L);
        verify(mockTicketController, times(1)).removePerson(2L, 1L);

        assertTrue(testPerson.getTicketsId().isEmpty());
    }

    @Test
    void rename() throws PersonNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockPersonDatabase).update(any());

        Person testPerson = spy(new Person(1L, "foo"));

        assertThrows(PersonNotFoundException.class,() -> controller.rename(1L, "faa"));

        assertEquals(testPerson.getName(), "foo");
        verify(mockPersonDatabase, times(1)).getById(any());
        verify(mockPersonDatabase, never()).update(any());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());

        controller.rename(1L, "faa");

        assertEquals(testPerson.getName(), "faa");
        verify(mockPersonDatabase, times(2)).getById(any());
        verify(mockPersonDatabase, times(1)).update(any());
    }

    @Test
    void modifyDebt() throws PersonNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockPersonDatabase).update(any());

        Person testPerson1 = new Person(1L, "foo");
        Person testPerson2 = new Person(2L, "bar");

        assertThrows(PersonNotFoundException.class, () -> controller.modifyDebt(1L, 2L, 100));

        assertTrue(testPerson1.getDebts().isEmpty());
        assertTrue(testPerson2.getDebts().isEmpty());
        verify(mockPersonDatabase, times(2)).getById(any());
        verify(mockPersonDatabase, never()).update(any());

        doReturn(Optional.of(testPerson1)).when(mockPersonDatabase).getById(1L);

        assertThrows(PersonNotFoundException.class, () -> controller.modifyDebt(1L, 2L, 100));

        assertTrue(testPerson1.getDebts().isEmpty());
        assertTrue(testPerson2.getDebts().isEmpty());
        verify(mockPersonDatabase, times(4)).getById(any());
        verify(mockPersonDatabase, times(0)).update(any());

        doReturn(Optional.of(testPerson2)).when(mockPersonDatabase).getById(2L);

        controller.modifyDebt(1L, 2L, 100);

        assertFalse(testPerson1.getDebts().isEmpty());
        assertTrue(testPerson1.getDebts().containsKey(2L));
        assertEquals(testPerson1.getDebts().get(2L), 100);
        assertTrue(testPerson2.getDebts().isEmpty());
        verify(mockPersonDatabase, times(6)).getById(any());
        verify(mockPersonDatabase, times(1)).update(any());

        controller.modifyDebt(1L, 2L, -50);
        assertEquals(testPerson1.getDebts().get(2L), 50);
    }

    @Test
    void resetDebt() throws PersonNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());

        assertThrows(PersonNotFoundException.class, () -> controller.resetDebt(1L));

        verify(mockPersonDatabase, times(1)).getById(any());

        Person testPerson = new Person(1L, "foo");
        testPerson.getDebts().put(1L, 100.);
        testPerson.getDebts().put(2L, -50.);

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());

        assertFalse(testPerson.getDebts().isEmpty());

        controller.resetDebt(1L);

        assertTrue(testPerson.getDebts().isEmpty());
        verify(mockPersonDatabase, times(2)).getById(any());
    }

    @Test
    void pay() throws PersonNotFoundException, CategoryNotFoundException, TicketNotFoundException {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketController).create(any(Long.class),any(Double.class),any());

        Optional<Ticket> receivedTicket;

        assertThrows(PersonNotFoundException.class, () -> controller.pay(1L,2L,100.));


        verify(mockPersonDatabase, times(1)).getById(1L);
        verify(mockPersonDatabase, never()).getById(2L);
        verify(mockTicketController, never()).create(any(Long.class),any(Double.class),any());

        Person payingPerson = new Person(1L,"foo");
        doReturn(Optional.of(payingPerson)).when(mockPersonDatabase).getById(1L);

        assertThrows(PersonNotFoundException.class, () -> controller.pay(1L,2L,100.));


        verify(mockPersonDatabase, times(2)).getById(1L);
        verify(mockPersonDatabase, times(1)).getById(2L);
        verify(mockTicketController, never()).create(any(Long.class),any(Double.class),any());

        Person receivingPerson = new Person(2L,"bar");
        doReturn(Optional.of(receivingPerson)).when(mockPersonDatabase).getById(2L);

        receivedTicket = controller.pay(1L,2L,100.);

        assertTrue(receivedTicket.isEmpty());

        verify(mockPersonDatabase, times(3)).getById(1L);
        verify(mockPersonDatabase, times(2)).getById(2L);
        verify(mockTicketController, times(1)).create(any(),any(Double.class),any());


        Ticket testTicket = new Ticket(1L, 100.,0L);
        doReturn(Optional.of(testTicket)).when(mockTicketController).create(any(),any(Double.class),any());

        receivedTicket = controller.pay(1L,2L,100);

        assertTrue(receivedTicket.isPresent());
        assertEquals(receivedTicket.get().getPayerId(),payingPerson.getId());
        assertEquals(receivedTicket.get().getDistribution().get(2L),100.);
        assertEquals(receivedTicket.get().getDistribution().get(1L),0.);

        verify(mockPersonDatabase, times(4)).getById(1L);
        verify(mockPersonDatabase, times(3)).getById(2L);
        verify(mockTicketController, times(2)).create(any(),any(Double.class),any());
        verify(mockTicketController, never()).create(any(),any(Double.class), isNull());
    }
}
