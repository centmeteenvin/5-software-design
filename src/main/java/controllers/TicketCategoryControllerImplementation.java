package controllers;

import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class TicketCategoryControllerImplementation extends TicketCategoryController {
    private TicketController ticketController;

    protected TicketCategoryControllerImplementation(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        super(ticketCategoryDatabase, ticketDatabase);
    }

    @Override
    public void setTicketController(TicketController ticketController) {
        this.ticketController = ticketController;
    }

    @Override
    public Optional<TicketCategory> create(String name) {
        long id = System.nanoTime();
        Optional<TicketCategory> ticketCategory = ticketCategoryDatabase.create(new TicketCategory(id, name));

        return ticketCategory;
    }

    @Override
    public void addTicket(Long id, Long ticketId) {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isEmpty()) return;

        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (ticket.isEmpty()) return;

        category.get().getTicketIds().add(ticketId);
        ticketCategoryDatabase.update(category.get());
    }

    @Override
    public void removeTicket(Long id, Long ticketId) {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isEmpty()) return;

        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (ticket.isEmpty()) return;

        try {
            category.get().getTicketIds().remove(ticketId);
            ticketCategoryDatabase.update(category.get());
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void rename(Long id, String newName) {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isPresent()) {
            category.get().setName(newName);
            ticketCategoryDatabase.update(category.get());
        }
    }

    @Override
    public void delete(Long id) {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isPresent()) {
            Iterator<Long> it = category.get().getTicketIds().iterator();
            while (it.hasNext()){
                long ticketId = it.next();
                ticketController.changeCategory(ticketId,0L);
                it.remove();
            }
            ticketCategoryDatabase.deleteById(id);
        }
    }
}
