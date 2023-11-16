package controllers;

import database.Database;
import models.Person;
import models.Ticket;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public class PersonControllerConcrete extends PersonController {
    private TicketController ticketController;

    protected PersonControllerConcrete(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
        super(personDatabase, ticketDatabase);
    }

    /**
     * Sets the instance of the ticketController
     * @param ticketController
     */
    @Override
    public void setTicketController(TicketController ticketController) {
        this.ticketController = ticketController;
    }

    /**
     * Creates the person and puts it in the database.
     * @param name
     * @return
     */
    @Override
    public Optional<Person> create(String name) {
        long id = System.nanoTime();

        Optional<Person> person = personDatabase.create(new Person(id, name, 0));

        return person;

    }

    /**
     * Adds the ticket to the persons ticketIdList and updates the PersonDatabase.
     *
     * @param id
     * @param ticketId
     */
    @Override
    public void addTicket(Long id, Long ticketId) {
        Optional<Person> person = personDatabase.getById(id);
        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (person.isPresent() && ticket.isPresent()) {
            person.get().getTicketsId().add(ticketId);
            personDatabase.update(person.get());
        }
    }

    /**
     * Removes a ticket from the persons ticketIdList and updates the PersonDatabase
     *
     * @param id
     * @param ticketId
     */
    @Override
    public void removeTicket(Long id, Long ticketId) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            List<Long> ticketIds = person.get().getTicketsId();
            ticketIds.remove(ticketId);
            personDatabase.update(person.get());
        }
    }

    /**
     * Deletes the person from the database and the person from all the tickets.
     * Uses {@link TicketController#removePerson(Long, Long)}
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            personDatabase.deleteById(id);
            for (long ticketId : person.get().getTicketsId()) {
                ticketController.removePerson(id, ticketId);
                person.get().getTicketsId().remove(ticketId);
            }
        }
    }

    /**
     * Changes the name of the person and updates the database.
     *
     * @param id
     * @param newName
     */
    @Override
    public void rename(Long id, String newName) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            person.get().setName(newName);
            personDatabase.update(person.get());
        }
    }

    /**
     * Adds the difference to the persons debt
     *
     * @param id
     * @param difference the difference that is ADDED to the current person's debt.
     */
    @Override
    public void modifyDebt(Long id, double difference) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            person.get().setDebt(person.get().getDebt() + difference);
            personDatabase.update(person.get());
        }
    }
}