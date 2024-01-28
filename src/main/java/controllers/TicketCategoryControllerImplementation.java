package controllers;

import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
import models.Ticket;
import models.TicketCategory;

import java.util.Iterator;
import java.util.List;
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
    public void delete(Long id) throws CategoryNotFoundException {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(id);
        if (category.isEmpty()) throw new CategoryNotFoundException(id);
        List<Long> ticketIds = List.copyOf(category.get().getTicketIds());
        for (Long ticketId : ticketIds) {
            try {
                ticketController.changeCategory(ticketId, null);
            } catch (TicketNotFoundException e) {
                //pass, if a ticket is not found, it need not be deleted another time.
            }
        }
        ticketCategoryDatabase.deleteById(id);
    }
}
