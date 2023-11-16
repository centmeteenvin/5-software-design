package controllers;

import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PersonControllerImplementationTest {
    private Database<Person> mockPersonDatabase;
    private Database<Ticket> mockTicketDatabase;
    private Database<TicketCategory> mockTicketCategoryDatabase;

    private TicketController mockTicketController;
    private PersonControllerConcrete controller;
    private TicketCategoryController mockTicketCategoryController;

    @BeforeEach
    void setup() {
        mockTicketDatabase = (Database<Ticket>) mock(Database.class);
        mockPersonDatabase = (Database<Person>) mock(Database.class);
        mockTicketCategoryDatabase = (Database<TicketCategory>) mock(Database.class);

        mockTicketController = mock(TicketController.class);
        mockTicketCategoryController = mock(TicketCategoryController.class);

        controller = new PersonControllerConcrete(mockPersonDatabase, mockTicketDatabase);
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

        Person person = new Person(1L, "foo1", 0);
        doReturn(Optional.of(person)).when(mockPersonDatabase).create(any());
        receivedPerson = controller.create("foo1");
        assertTrue(receivedPerson.isPresent());
        assertEquals(receivedPerson.get(), person);
    }

    @Test
    void addTicket() {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());

        Person testPerson = new Person(1L, "foo", 0);
        Ticket testTicket = new Ticket(2L, 100, 1L);
        controller.addTicket(testPerson.getId(), 2L);

        assertTrue(testPerson.getTicketsId().isEmpty());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());
        controller.addTicket(testPerson.getId(), 2L);

        assertTrue(testPerson.getTicketsId().isEmpty());

        doReturn(Optional.of(testTicket)).when(mockTicketDatabase).getById(any());
        controller.addTicket(testPerson.getId(), 2L);

        assertEquals(testPerson.getTicketsId().size(), 1);
        assertEquals(testPerson.getTicketsId().get(0), 2L);

        // Make sure ticketController is not used, otherwise cyclic reference
        verifyNoInteractions(mockTicketController);
    }

    @Test
    void removeTicket() {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockPersonDatabase).update(any());

        Person testPerson = new Person(1L, "foo", 0);
        testPerson.getTicketsId().add(1L);

        controller.removeTicket(testPerson.getId(), 1L);

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
    void Delete() {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doNothing().when(mockPersonDatabase).deleteById(any());
        doNothing().when(mockTicketController).removePerson(any(), any());

        Person testPerson = spy(new Person(1L, "foo", 0));

        // Do nothing (Check)
        controller.delete(1L);

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
        verify(mockTicketController, times(2)).removePerson(any(),any());
        assertTrue(testPerson.getTicketsId().isEmpty());
    }

    @Test
    void rename(){
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockPersonDatabase).update(any());

        Person testPerson = spy(new Person(1L,"foo",0));

        controller.rename(1L,"faa");

        assertEquals(testPerson.getName(),"foo");
        verify(mockPersonDatabase, times(1)).getById(any());
        verify(mockPersonDatabase, never()).update(any());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());

        controller.rename(1L,"faa");

        assertEquals(testPerson.getName(),"faa");
        verify(mockPersonDatabase, times(2)).getById(any());
        verify(mockPersonDatabase, times(1)).update(any());
    }

    @Test
    void modifyDebt() {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockPersonDatabase).update(any());

        Person testPerson = spy(new Person(1L,"foo",0));

        controller.modifyDebt(1L,10);

        assertEquals(testPerson.getDebt(),0);
        verify(mockPersonDatabase, times(1)).getById(any());
        verify(mockPersonDatabase, never()).update(any());

        doReturn(Optional.of(testPerson)).when(mockPersonDatabase).getById(any());

        controller.modifyDebt(1L,10);

        assertEquals(testPerson.getDebt(),10);
        verify(mockPersonDatabase, times(2)).getById(any());
        verify(mockPersonDatabase, times(1)).update(any());
    }
}