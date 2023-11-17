package controllers;

import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

import java.util.List;
import java.util.Optional;

public abstract class TicketController {
    protected final Database<Ticket> ticketDatabase;
    protected final Database<Person> personDatabase;
    protected final Database<TicketCategory> ticketCategoryDatabase;

    protected PersonController personController;
    protected TicketCategoryController ticketCategoryController;
    protected TicketController(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase) {
        this.ticketDatabase = ticketDatabase;
        this.personDatabase = personDatabase;
        this.ticketCategoryDatabase = ticketCategoryDatabase;
    }

    /**
     * Creates a new ticket and stores it in the db. If the categoryId does not exist or any of the personIds do not exist. it will return empty.
     */
    public abstract Optional<Ticket> create(Long categoryId, double totalCost, List<Long> personsId);

    /**
     * Adds a person to the ticket. SHOULD call {@link PersonController#addTicket(Long, Long)}.
     */
    public abstract void addPerson(Long id, Long personId);

    /**
     * Removes a person from the ticket. SHOULD call {@link PersonController#removeTicket(Long, Long)}
     */
    public abstract void removePerson(Long id, Long personId);

    /**
     * Changes the current category. SHOULD call {@link TicketCategoryController#removeTicket(Long, Long)} and {@link TicketCategoryController#addTicket(Long, Long)}
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

    /**
     * Sets the payer to a certain id
     * @param id
     */
    public abstract void setPayer(Long id, Long payerId);

    public final void setPersonController(PersonController personController) {
        this.personController = personController;
    }
    public final void setTicketCategoryController(TicketCategoryController ticketCategoryController) {
        this.ticketCategoryController = ticketCategoryController;
    }
}
