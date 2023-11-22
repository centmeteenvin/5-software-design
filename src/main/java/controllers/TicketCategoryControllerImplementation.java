package controllers;

import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
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
        return ticketCategoryDatabase.create(new TicketCategory(id, name));
    }

    @Override
    public void addTicket(Long id, Long ticketId) throws CategoryNotFoundException, TicketNotFoundException {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException(id);

        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (ticket.isEmpty()) throw new TicketNotFoundException(ticketId);

        category.get().getTicketIds().add(ticketId);
        ticketCategoryDatabase.update(category.get());
    }

    @Override
    public void removeTicket(Long id, Long ticketId) throws CategoryNotFoundException, TicketNotFoundException {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException(id);

        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (ticket.isEmpty()) throw new TicketNotFoundException(ticketId);

        if (!category.get().getTicketIds().contains(ticketId)) return;

        category.get().getTicketIds().remove(ticketId);
        ticketCategoryDatabase.update(category.get());

    }

    @Override
    public void rename(Long id, String newName) throws CategoryNotFoundException {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException(id);
        category.get().setName(newName);
        ticketCategoryDatabase.update(category.get());
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
