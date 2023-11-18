package controllers;

import database.Database;
import models.Ticket;
import models.TicketCategory;

import java.util.Iterator;
import java.util.Optional;

public class TicketCategoryControllerImplementation extends TicketCategoryController {
    public TicketCategoryControllerImplementation(Database<TicketCategory> ticketCategoryDatabase, Database<Ticket> ticketDatabase) {
        super(ticketCategoryDatabase, ticketDatabase);
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

        if (!category.get().getTicketIds().contains(ticketId)) return;

        category.get().getTicketIds().remove(ticketId);
        ticketCategoryDatabase.update(category.get());

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
            while (it.hasNext()) {
                long ticketId = it.next();
                ticketController.changeCategory(ticketId, null);
                it.remove();
            }
            ticketCategoryDatabase.deleteById(id);
        }
    }
}
