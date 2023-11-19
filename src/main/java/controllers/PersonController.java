package controllers;

import database.Database;
import models.Person;
import models.Ticket;

import java.util.Optional;

public abstract class PersonController {
    protected final Database<Person> personDatabase;
    protected final Database<Ticket> ticketDatabase;
    protected TicketController ticketController;
    public PersonController(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
        this.personDatabase = personDatabase;
        this.ticketDatabase = ticketDatabase;
    }

    public final void setTicketController(TicketController ticketController) {
        this.ticketController = ticketController;
    }

    /**
     * Creates a new person. Identifier is generated randomly. debt is initialized at 0.
     * Is stored directly into the db.
     */
    public abstract Optional<Person> create(String name);

    /**
     * Adds a ticket to {@link Person#getTicketsId()}. Should NOT call {@link TicketController#addPerson(Long, Long)}.
     */
    public abstract void addTicket(Long id, Long ticketId);

    /**
     * Removes a ticket from the {@link Person#getTicketsId()}. Should NOT call {@link TicketController#removePerson(Long, Long)}.
     */
    public abstract void removeTicket(Long id, Long ticketId);

    /**
     * Removes the current person from the database. SHOULD call {@link TicketController#addPerson(Long, Long)}
     */
    public abstract void delete(Long id);

    /**
     * Rename the current person.
     */
    public abstract void rename(Long id, String newName);

    /**
     * Modifies the person's debt.
     * @param id The id of the person's debts we want to modify.
     * @param otherPersonId The id of the other person. This is the line in the Person's debt we want to change.
     * @param difference the difference that is ADDED to the current person's debt.
     */
    public abstract void modifyDebt(Long id, Long otherPersonId, double difference);

}
