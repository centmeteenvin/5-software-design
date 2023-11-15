package controllers;

import database.Database;
import models.Ticket;
import models.TicketCategory;

public abstract class TicketCategoryController {
    private final Database<TicketCategory> ticketCategoryDatabase;
    private final Database<Ticket> ticketDatabase;

    protected TicketCategoryController(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        this.ticketCategoryDatabase = ticketCategoryDatabase;
        this.ticketDatabase = ticketDatabase;
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
