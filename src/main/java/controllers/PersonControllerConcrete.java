package controllers;

import database.Database;
import models.Person;

public class PersonControllerConcrete extends PersonController{
    protected PersonControllerConcrete(Database<Person> personDatabase) {
        super(personDatabase);
    }

    @Override
    public void setTicketController(TicketController ticketController) {

    }

    @Override
    public Person create(String name) {
        return null;
    }

    @Override
    public void addTicket(Long id, Long ticketId) {

    }

    @Override
    public void removeTicket(Long id, Long ticketId) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void rename(Long id, String newName) {

    }

    @Override
    public void modifyDebt(Long id, double difference) {

    }
}
