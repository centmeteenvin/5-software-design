package controllers;

import database.Database;
import models.Person;
import models.Ticket;

import java.util.*;

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
    public void addTicket(Long id, Long ticketId) {
        Optional<Person> person = personDatabase.getById(id);
        Optional<Ticket> ticket = ticketDatabase.getById(ticketId);
        if (person.isPresent() && ticket.isPresent()) {
            person.get().getTicketsId().add(ticketId);
            personDatabase.update(person.get());
        }
    }

    /**
     * Removes a ticket from the persons ticketIdList and updates the PersonDatabase
     *
     * @param id
     * @param ticketId
     */
    @Override
    public void removeTicket(Long id, Long ticketId) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            List<Long> ticketIds = person.get().getTicketsId();
            ticketIds.remove(ticketId);
            personDatabase.update(person.get());
        }
    }

    /**
     * Deletes the person from the database and the person from all the tickets.
     * Uses {@link TicketController#removePerson(Long, Long)}
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            Iterator<Long> it = person.get().getTicketsId().iterator();
            while (it.hasNext()) {
                long ticketId = it.next();
                ticketController.removePerson(id, ticketId);
                it.remove();
            }
            personDatabase.deleteById(id);
        }
    }

    /**
     * Changes the name of the person and updates the database.
     *
     * @param id
     * @param newName
     */
    @Override
    public void rename(Long id, String newName) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isPresent()) {
            person.get().setName(newName);
            personDatabase.update(person.get());
        }
    }

    /**
     * Adds the difference to the person's debt.
     *
     * @param difference the difference that is ADDED to the current person's debt.
     */
    @Override
    public void modifyDebt(Long id, Long otherId, double difference) {
        Optional<Person> person = personDatabase.getById(id);
        Optional<Person> subject = personDatabase.getById(otherId);
        if (person.isEmpty() || subject.isEmpty()) return;
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
    public void resetDebt(Long id) {
        Optional<Person> person = personDatabase.getById(id);
        if (person.isEmpty()) return;

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
    public Optional<Ticket> pay(Long payerId, Long receivingPersonId, double payedAmount) {
        Optional<Person> payer = personDatabase.getById(payerId);
        if (payer.isEmpty()) return Optional.empty();

        Optional<Person> receiver = personDatabase.getById(receivingPersonId);
        if (receiver.isEmpty()) return Optional.empty();

        Optional<Ticket> ticket = ticketController.create(0L, payedAmount, null);
        if (ticket.isEmpty()) return ticket;

        ticket.get().setPayerId(payerId);
        ticket.get().getDistribution().put(receivingPersonId, payedAmount);
        return ticket;
    }
}
