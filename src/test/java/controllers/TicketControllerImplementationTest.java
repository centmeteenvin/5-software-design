package controllers;

import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TicketControllerImplementationTest {

    private Database<Ticket> mockTicketDatabase;
    private Database<Person> mockPersonDatabase;
    private Database<TicketCategory> mockTicketCategoryDatabase;

    private TicketControllerImplementation controller;
    private PersonController mockPersonController;
    private TicketCategoryController mockTicketCategoryController;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        mockTicketDatabase = (Database<Ticket>) mock(Database.class);
        mockPersonDatabase = (Database<Person>) mock(Database.class);
        mockTicketCategoryDatabase = (Database<TicketCategory>) mock(Database.class);

        mockPersonController = mock(PersonController.class);
        mockTicketCategoryController = mock(TicketCategoryController.class);

        controller = new TicketControllerImplementation(mockTicketDatabase, mockPersonDatabase, mockTicketCategoryDatabase);
        controller.setTicketCategoryController(mockTicketCategoryController);
        controller.setPersonController(mockPersonController);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).getById(any());
        doNothing().when(mockPersonController).addTicket(any(), any());
        doNothing().when(mockTicketCategoryController).addTicket(any(), any());

        Optional<Ticket> receivedTicket  = controller.create(1L, 100, List.of(2L));
        assertTrue(receivedTicket.isEmpty());

        TicketCategory category = new TicketCategory(1L, "foo");
        doReturn(Optional.of(category)).when(mockTicketCategoryDatabase).getById(1L);
        receivedTicket = controller.create(1L, 100, List.of(2L));
        assertTrue(receivedTicket.isEmpty());

        Person person = new Person(2L, "bar", 0);
        Ticket ticket = new Ticket(3L, 100, 1L);
        ticket.getDistribution().put(1L, 100D);
        doReturn(Optional.of(person)).when(mockPersonDatabase).getById(2L);
        doReturn(Optional.of(ticket)).when(mockTicketDatabase).create(any());
        receivedTicket = controller.create(1L, 100, List.of(2L));
        assertTrue(receivedTicket.isPresent());
        assertEquals(receivedTicket.get(), ticket);
        verify(mockPersonController, times(1)).addTicket(any(), any());
        verify(mockTicketCategoryController, times(1)).addTicket(any(), any());

    }

    @Test
    void addPerson() {
        doReturn(Optional.empty()).when(mockTicketDatabase).update(any());
        doNothing().when(mockPersonController).addTicket(any(), any());
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());


        controller.addPerson(1L, 1L);
        Person person = new Person(2L, "bar", 0);
        Ticket ticket = new Ticket(3L, 100, 1L);

        doReturn(Optional.of(person)).when(mockPersonDatabase).getById(any());
        controller.addPerson(1L, 1L);

        verify(mockTicketDatabase, never()).update(any());
        verify(mockPersonController, never()).addTicket(any(), any());

        doReturn(Optional.of(ticket)).when(mockTicketDatabase).getById(any());

        controller.addPerson(1L, 1L);
        assertEquals(ticket.getDistribution().get(1L), 0);
        verify(mockTicketDatabase, times(1)).update(any());
        verify(mockPersonController, times(1)).addTicket(any(), any());

        ticket.getDistribution().put(1L, 0D);
        controller.addPerson(1L, 1L);
        verify(mockTicketDatabase, times(1)).update(any());
        verify(mockPersonController, times(1)).addTicket(any(), any());
    }

    @Test
    void removePerson() {
        doReturn(Optional.empty()).when(mockTicketDatabase).update(any());
        doNothing().when(mockPersonController).removeTicket(any(), any());
        doReturn(Optional.empty()).when(mockPersonDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());


        controller.removePerson(1L, 1L);
        Person person = new Person(1L, "bar", 0);
        Ticket ticket = new Ticket(1L, 100, 1L);

        doReturn(Optional.of(ticket)).when(mockTicketDatabase).getById(any());

        controller.addPerson(1L, 1L);
        verify(mockTicketDatabase, never()).update(any());

        ticket.getDistribution().put(1L, 0D);
        controller.removePerson(1L, 1L);

        verify(mockTicketDatabase, times(1)).update(any());
        verify(mockPersonController, never()).removeTicket(any(), any());

        doReturn(Optional.of(person)).when(mockPersonDatabase).getById(any());
        ticket.getDistribution().put(1L, 0D);

        controller.removePerson(1L, 1L);
        assertTrue(ticket.getDistribution().isEmpty());
        verify(mockTicketDatabase, times(2)).update(any());
        verify(mockPersonController, times(1)).removeTicket(any(), any());
    }

    @Test
    void changeCategory() {
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).update(any());
        doReturn(Optional.empty()).when(mockTicketCategoryDatabase).getById(any());
        doNothing().when(mockTicketCategoryController).addTicket(any(), any());
        doNothing().when(mockTicketCategoryController).removeTicket(any(), any());

        TicketCategory category1 = new TicketCategory(1L, "foo");
        TicketCategory category2 = new TicketCategory(2L, "bar");
        Ticket ticket = new Ticket(1L, 100, 1L);

        controller.changeCategory(1L, 2L);

        doReturn(Optional.of(ticket)).when(mockTicketDatabase).getById(any());

        controller.changeCategory(1L, 2L);

        doReturn(Optional.of(category1)).when(mockTicketCategoryDatabase).getById(1L);

        controller.changeCategory(1L, 2L);
        controller.changeCategory(1L, 1L);

        verify(mockTicketDatabase, never()).update(any());
        verify(mockTicketCategoryController, never()).addTicket(any(), any());
        verify(mockTicketCategoryController, never()).removeTicket(any(), any());

        doReturn(Optional.of(category2)).when(mockTicketCategoryDatabase).getById(2L);
        controller.changeCategory(1L, 2L);

        assertEquals(ticket.getTicketCategoryId(), 2L);
        verify(mockTicketDatabase, times(1)).update(any());
        verify(mockTicketCategoryController, times(1)).addTicket(any(), any());
        verify(mockTicketCategoryController, times(1)).removeTicket(any(), any());
    }

    @Test
    void changeCost() {
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).update(any());
        Ticket ticket = new Ticket(1L, 100, 1L);
        controller.changeCost(1L, 200);

        verify(mockTicketDatabase, never()).update(any());

        doReturn(Optional.of(ticket)).when(mockTicketDatabase).getById(any());

        controller.changeCost(1L, 200);

        assertEquals(ticket.getCost(), 200);
        verify(mockTicketDatabase, times(1)).update(any());
    }

    @Test
    void changeWeight() {
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());
        doReturn(Optional.empty()).when(mockTicketDatabase).update(any());
        Ticket ticket = new Ticket(1L, 100, 1L);
        controller.changeWeight(1L, 1L, 100);

        doReturn(Optional.of(ticket)).when(mockTicketDatabase).getById(any());

        controller.changeWeight(1L, 1L, 100);

        verify(mockTicketDatabase, never()).update(any());

        ticket.getDistribution().put(1L, 0D);

        controller.changeWeight(1L, 1L, 100);

        assertEquals(ticket.getDistribution().get(1L), 100);
        verify(mockTicketDatabase, times(1)).update(any());
    }

    @Test
    void delete() {
        doReturn(Optional.empty()).when(mockTicketDatabase).getById(any());
        doNothing().when(mockTicketDatabase).deleteById(any());
        doNothing().when(mockPersonController).removeTicket(any(), any());
        doNothing().when(mockTicketCategoryController).removeTicket(any(), any());
        Ticket ticket = new Ticket(1L, 100, 1L);

        controller.delete(1L);

        verify(mockTicketDatabase, never()).deleteById(any());
        verify(mockPersonController, never()).removeTicket(any(), any());
        verify(mockTicketCategoryController, never()).removeTicket(any(), any());

        doReturn(Optional.of(ticket)).when(mockTicketDatabase).getById(any());

        controller.delete(1L);

        verify(mockTicketDatabase, times(1)).deleteById(any());
        verify(mockTicketCategoryController, times(1)).removeTicket(any(), any());
        verify(mockPersonController, never()).removeTicket(any(), any());

        ticket.getDistribution().put(1L, 0D);
        controller.delete(1L);

        verify(mockTicketDatabase, times(2)).deleteById(any());
        verify(mockTicketCategoryController, times(2)).removeTicket(any(), any());
        verify(mockPersonController, times(1)).removeTicket(any(), any());
    }

    @Test
    void calculate() {
    }
}