package controllers;

import database.Database;
import models.TicketCategory;

public abstract class TicketCategoryController {
    private final Database<TicketCategory> ticketCategoryDatabase;

    protected TicketCategoryController(Database<TicketCategory> ticketCategoryDatabase) {
        this.ticketCategoryDatabase = ticketCategoryDatabase;
    }

    /**
     * Creates a ticketCategory with the given name.
     */
    public abstract TicketCategory create(String Name);

    /**
     * adds a ticket to the current category.
     */
    public abstract void addTicket(Long id, Long ticketId);

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
    public abstract void setTicketController(TicketController ticketController);
}
