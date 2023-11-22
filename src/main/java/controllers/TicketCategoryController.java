package controllers;

import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
import models.Ticket;
import models.TicketCategory;

import java.util.Optional;

public abstract class TicketCategoryController {
    protected final Database<TicketCategory> ticketCategoryDatabase;
    protected final Database<Ticket> ticketDatabase;
    protected TicketController ticketController;

    public TicketCategoryController(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        this.ticketCategoryDatabase = ticketCategoryDatabase;
        this.ticketDatabase = ticketDatabase;
    }

    /**
     * Creates a ticketCategory with the given name.
     */
    public abstract Optional<TicketCategory> create(String name);

    /**
     * adds a ticket to the current category.
     */
    public abstract void addTicket(Long id, Long ticketId) throws CategoryNotFoundException, TicketNotFoundException;

    /**
     * removes a ticket from the current category. SHOULD call {@link TicketController#changeCategory(Long, Long)}.
     */
    public abstract void removeTicket(Long id, Long ticketId);

    /**
     * renames the current ticket name.
     */
    public abstract void rename(Long id, String newName);

    /**
     * Deletes the category. SHOULD call {@link TicketController#changeCategory(Long, Long)}.
     */
    public abstract void delete(Long id);

    public final void setTicketController(TicketController ticketController) {
        this.ticketController = ticketController;
    }
}
