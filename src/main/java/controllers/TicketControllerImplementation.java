package controllers;

import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

import java.util.*;


public class TicketControllerImplementation extends TicketController {


    protected TicketControllerImplementation(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase) {
        super(ticketDatabase, personDatabase, ticketCategoryDatabase);
    }

    /**
     * Creates a new ticket and stores it in the db.
     */
    @Override
    public Optional<Ticket> create(Long categoryId, double totalCost, List<Long> personsId) {
        Optional<TicketCategory> category = ticketCategoryDatabase.getById(categoryId);
        if (category.isEmpty()) return Optional.empty();

        List<Person> people = new ArrayList<>();
        for (Long personId : personsId) {
            Optional<Person> person = personDatabase.getById(personId);
            if (person.isEmpty()) return Optional.empty();
            people.add(person.get());
        }

        Optional<Ticket> ticket = ticketDatabase.create(new Ticket(System.nanoTime(), totalCost, categoryId));
        if (ticket.isEmpty()) return Optional.empty();
        ticketCategoryController.addTicket(categoryId, ticket.get().getId());
        for (Long personId : personsId) {
            personController.addTicket(personId, ticket.get().getId());
            ticket.get().getDistribution().put(personId, totalCost/personsId.size());
        }
        return ticket;
    }

    /**
     * Adds a person to the ticket. SHOULD call {@link PersonController#addTicket(Long, Long)}.
     */
    @Override
    public void addPerson(Long id, Long personId) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        Optional<Person> person = personDatabase.getById(personId);
        if (person.isEmpty()) return;
        if (ticket.get().getDistribution().containsKey(personId)) return;
        Map<Long, Double> distribution = ticket.get().getDistribution();
        distribution.put(personId, 0D);
        ticketDatabase.update(ticket.get());
        personController.addTicket(personId, id);
    }

    /**
     * Removes a person from the ticket. SHOULD call {@link PersonController#removeTicket(Long, Long)}
     */
    @Override
    public void removePerson(Long id, Long personId) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        if (!ticket.get().getDistribution().containsKey(personId)) return;
        ticket.get().getDistribution().remove(personId);
        ticketDatabase.update(ticket.get());
        Optional<Person> person = personDatabase.getById(personId);
        if (person.isEmpty()) return;
        personController.removeTicket(personId, id);
    }

    /**
     * Changes the current category.
     */
    @Override
    public void changeCategory(Long id, Long newCategoryId) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        Optional<TicketCategory> newCategory = ticketCategoryDatabase.getById(newCategoryId);
        if (newCategory.isEmpty()) return;
        if (Objects.equals(ticket.get().getTicketCategoryId(), newCategoryId)) return;
        Long oldTicketCategoryId = ticket.get().getTicketCategoryId();
        ticket.get().setTicketCategoryId(newCategoryId);
        ticketDatabase.update(ticket.get());
        ticketCategoryController.removeTicket(oldTicketCategoryId, id);
        ticketCategoryController.addTicket(newCategoryId, id);
    }

    /**
     * Changes the current cost of the ticket. CONSIDER calling  {@link #calculate(Long)}.
     */
    @Override
    public void changeCost(Long id, double newTotalCost) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        ticket.get().setCost(newTotalCost);
        ticketDatabase.update(ticket.get());
    }

    /**
     * Updated the weight of a certain person. Consider calling  {@link #calculate(Long)}.
     */
    @Override
    public void changeWeight(Long id, Long personId, double newWeight) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        if (!ticket.get().getDistribution().containsKey(personId)) return;
        ticket.get().getDistribution().put(personId, newWeight);
        ticketDatabase.update(ticket.get());
    }

    /**
     * Deletes the Ticket. SHOULD call {@link PersonController#removeTicket(Long, Long)} AND {@link TicketCategoryController#removeTicket(Long, Long)}.
     */
    @Override
    public void delete(Long id) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        ticketDatabase.deleteById(id);
        ticketCategoryController.removeTicket(ticket.get().getTicketCategoryId(), id);
        for (Long personId : ticket.get().getDistribution().keySet()) {
            personController.removeTicket(personId, id);
        }
    }

    /**
     * Updates {@link Person#getDebt()} for every person in the ticket via {@link PersonController#modifyDebt(Long, double)}.
     */
    @Override
    public void calculate(Long id) {

    }
}
