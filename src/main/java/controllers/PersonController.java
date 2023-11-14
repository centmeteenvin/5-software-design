package controllers;

import database.Database;
import models.Person;

public abstract class PersonController {
    private final Database<Person> personDatabase;

    protected PersonController(Database<Person> personDatabase) {
        this.personDatabase = personDatabase;
    }

    public abstract void setTicketController(TicketController ticketController);

    /**
     * Creates a new person. Identifier is generated randomly. debt is initialized at 0.
     * Is stored directly into the db.
     */
    public abstract Person create(String name);

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
     * @param difference the difference that is ADDED to the current person's debt.
     */
    public abstract void modifyDebt(Long id, double difference);

}