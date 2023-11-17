package controllers;

import database.Database;
import models.Ticket;
import models.TicketCategory;

public class TicketCategoryControllerImplementation extends TicketCategoryController{
    protected TicketCategoryControllerImplementation(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        super(ticketCategoryDatabase, ticketDatabase);
    }

    @Override
    public TicketCategory create(String Name) {
        return null;
    }

    @Override
    public void addTicket(Long id, Long ticketId) {

    }

    @Override
    public void removeTicket(Long id, Long ticketId) {

    }

    @Override
    public void rename(Long id, String newName) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void setTicketController(TicketController ticketController) {

    }
}
