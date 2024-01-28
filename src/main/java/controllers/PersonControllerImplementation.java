package controllers;

import database.Database;
import exceptions.notFoundExceptions.CategoryNotFoundException;
import exceptions.notFoundExceptions.PersonNotFoundException;
import exceptions.notFoundExceptions.TicketNotFoundException;
import models.Person;
import models.Ticket;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PersonControllerImplementation extends PersonController {

    public PersonControllerImplementation(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
        super(personDatabase, ticketDatabase);
    }

    /**
     * Creates the person and puts it in the database.
     *
     * @param name
     * @return
     */
    @Override
    public Optional<Person> create(String name) {
        long id = System.nanoTime();
        Optional<Person> person = personDatabase.create(new Person(id, name));
        return person;

    }

    /**
     * Adds the ticket to the persons ticketIdList and updates the PersonDatabase.
     *
     * @param id
     * @param ticketId
     */
    @Override
    public void addTicket(Long id, Long ticketId) throws PersonNotFoundException, TicketNotFoundException {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isEmpty()) throw new PersonNotFoundException(id);
        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (ticket.isEmpty()) throw new TicketNotFoundException(ticketId);
        person.get().getTicketsId().add(ticketId);
        personDatabase.update(person.get());
    }

    /**
     * Removes a ticket from the persons ticketIdList and updates the PersonDatabase
     *
     * @param id
     * @param ticketId
     */
    @Override
    public void removeTicket(Long id, Long ticketId) throws PersonNotFoundException {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isEmpty()) throw new PersonNotFoundException(id);
        List<Long> ticketIds = person.get().getTicketsId();
        ticketIds.remove(ticketId);
        personDatabase.update(person.get());
    }

    /**
     * Deletes the person from the database and the person from all the tickets.
     * Uses {@link TicketController#removePerson(Long, Long)}
     *
     * @param id
     */
    @Override
    public void delete(Long id) throws PersonNotFoundException {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isEmpty()) throw new PersonNotFoundException(id);
        List<Long> personTicketIdsCopy = List.copyOf(person.get().getTicketsId());
        for (Long ticketId : personTicketIdsCopy) {
            try {
                ticketController.removePerson(ticketId, id);
            } catch (TicketNotFoundException e) {
                // pass if a ticket does not exist, we don't need to update its list.
            }
        }
        person.get().getTicketsId().clear();
        personDatabase.deleteById(id);
    }

    /**
     * Changes the name of the person and updates the database.
     *
     * @param id
     * @param newName
     */
    @Override
    public void rename(Long id, String newName) throws PersonNotFoundException {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isEmpty()) throw new PersonNotFoundException(id);
        person.get().setName(newName);
        personDatabase.update(person.get());
    }

    /**
     * Adds the difference to the person's debt.
     * This means there will be MORE debt to this person
     *
     * @param difference the difference that is ADDED to the current person's debt.
     */
    @Override
    public void modifyDebt(Long id, Long otherId, double difference) throws PersonNotFoundException {
        // Get the person that will have more debt
        Optional<Person> person = personDatabase.getById(id);
        // Get the person that will have less debt
        Optional<Person> subject = personDatabase.getById(otherId);
        if (person.isEmpty()) throw new PersonNotFoundException(id);
        if (subject.isEmpty()) throw new PersonNotFoundException(otherId);
        if (!person.get().getDebts().containsKey(otherId)) {
            person.get().getDebts().put(otherId, difference);
        } else {
            double previousDebt = person.get().getDebts().get(otherId);
            person.get().getDebts().put(otherId, previousDebt + difference);
        }
        personDatabase.update(person.get());
    }

    /**
     * Resets the debts map for a given personId.
     *
     * @param id
     */
    @Override
    public void resetDebt(Long id) throws PersonNotFoundException {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isEmpty()) throw new PersonNotFoundException(id);

        person.get().getDebts().clear();
    }

    /**
     * Makes a ticket that resembles the payerPerson paying to a receivingPerson.
     *
     * <p>
     * This is done by making an "inverted" ticket:
     * The person who receives will have a debt to the person that pays
     * This will counteract the current debt of the payer to the receiver
     * </p>
     *
     * @param payerId
     * @param receivingPersonId
     * @param payedAmount
     * @return
     */
    @Override
    public Optional<Ticket> pay(Long payerId, Long receivingPersonId, double payedAmount) throws PersonNotFoundException {
        Optional<Person> payer = personDatabase.getById(payerId);
        if (payer.isEmpty()) throw new PersonNotFoundException(payerId);

        Optional<Person> receiver = personDatabase.getById(receivingPersonId);
        if (receiver.isEmpty()) throw new PersonNotFoundException(receivingPersonId);

        Optional<Ticket> ticket = null;
        try {
            ticket = ticketController.create(null, payedAmount, List.of(payerId, receivingPersonId));
        } catch (CategoryNotFoundException e) {
            //pass
        }
        if (ticket.isEmpty()) return ticket;

        ticket.get().setPayerId(payerId);
        ticket.get().getDistribution().put(payerId, 0.);
        ticket.get().getDistribution().put(receivingPersonId, payedAmount);
        return ticket;
    }
}
