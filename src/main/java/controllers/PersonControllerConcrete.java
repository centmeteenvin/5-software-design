package controllers;

import database.Database;
import models.Person;
import models.Ticket;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PersonControllerConcrete extends PersonController {

    protected PersonControllerConcrete(Database<Person> personDatabase, Database<Ticket> ticketDatabase) {
        super(personDatabase, ticketDatabase);
    }

    /**
     * Creates the person and puts it in the database.
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
            while (it.hasNext()){
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
     * @param difference the difference that is ADDED to the current person's debt.
     */
    @Override
    public void modifyDebt(Long id, Long otherId,double difference) {
//        Optional<Person> person = personDatabase.getById(id);
//        if (person.isPresent()) {
//            person.get().setDebt(person.get().getDebt() + difference);
//            personDatabase.update(person.get());
//        }
        //TODO
    }
}
