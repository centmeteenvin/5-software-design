package controllers;

import database.Database;
import models.Person;
import models.Ticket;

import java.util.List;

public abstract class TicketController {
    private final Database<Ticket> ticketDatabase;

    protected TicketController(Database<Ticket> ticketDatabase) {
        this.ticketDatabase = ticketDatabase;
    }

    /**
     * Creates a new ticket and stores it in the db.
     */
    public abstract Ticket create(Long categoryId, double totalCost, List<Long> personsId);

    /**
     * Adds a person to the ticket. SHOULD call {@link PersonController#addTicket(Long, Long)}.
     */
    public abstract void addPerson(Long id, Long personId);

    /**
     * Removes a person from the ticket. SHOULD call {@link PersonController#removeTicket(Long, Long)}
     */
    public abstract void removePerson(Long id, Long personId);

    /**
     * Changes the current category.
     */
    public abstract void changeCategory(Long id, Long newCategoryId);

    /**
     * Changes the current cost of the ticket. CONSIDER calling  {@link #calculate(Long)}.
     */
    public abstract void changeCost(Long id, double newTotalCost);

    /**
     * Updated the weight of a certain person. Consider calling  {@link #calculate(Long)}.
     */
    public abstract void changeWeight(Long id, Long personId, double newWeight);

    /**
     * Deletes the Ticket. SHOULD call {@link PersonController#removeTicket(Long, Long)} AND {@link TicketCategoryController#removeTicket(Long, Long)}.
     */
    public abstract void delete(Long id);

    /**
     * Updates {@link Person#getDebt()} for every person in the ticket via {@link PersonController#modifyDebt(Long, double)}.
     * @param id
     */
    public abstract void calculate(Long id);
    public abstract void setPersonController(PersonController personController);
    public abstract void setTicketCategoryController(TicketCategoryController ticketCategoryController);
}
