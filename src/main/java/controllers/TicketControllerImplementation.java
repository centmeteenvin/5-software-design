package controllers;

import database.Database;
import models.Person;
import models.Ticket;
import models.TicketCategory;

import java.util.*;

import static java.lang.Long.sum;


public class TicketControllerImplementation extends TicketController {


    public TicketControllerImplementation(Database<Ticket> ticketDatabase, Database<Person> personDatabase, Database<TicketCategory> ticketCategoryDatabase) {
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
            ticket.get().getDistribution().put(personId, totalCost / personsId.size());
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

     */
    @Override
    public void calculate(Long id) {
        // Check the ticket is in the database
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;

        // Check that the total payed by everyone in the group is equal to the total cost of the ticket
        Map<Long, Double> distribution = ticket.get().getDistribution();
        if (distribution.values().stream().reduce(0., Double::sum) != ticket.get().getCost()) return;

        Optional<Person> payer;
        // Check if the payer is not null
        if (ticket.get().getPayerId() != null) {
            // Check if the payer still exists
            payer = personDatabase.getById(ticket.get().getPayerId());
            if (payer.isEmpty()) return;

            for (Long debtHolder : distribution.keySet()) {
                // Check if the debtHolder still exists
                Optional<Person> person = personDatabase.getById(debtHolder);
                if (person.isEmpty()) return;

                double difference = distribution.get(person);
                personController.modifyDebt(payer.get().getId(), debtHolder, difference);
                personController.modifyDebt(debtHolder, payer.get().getId(), -difference);
            }
        }
    }

    /**
     * Sets the payer in the ticket to the id of a Person or null when given specifically
     *
     * @param id
     * @param payerId
     */
    @Override
    public void setPayer(Long id, Long payerId) {
        Optional<Ticket> ticket = ticketDatabase.getById(id);
        if (ticket.isEmpty()) return;
        Optional<Person> person = personDatabase.getById(payerId);
        if (payerId == null || person.isPresent()) {
            ticket.get().setPayerId(payerId);
            ticketDatabase.update(ticket.get());
        }
    }
}
