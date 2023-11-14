package controllers;

import models.Person;

public interface PersonController {
    Person create(String name);
    void addTicket(Long id, Long ticketId);
    void removeTicket(Long id, Long ticketId);
    void delete(Long id);
    void rename(Long id, String newName);

    void setTicketController(TicketController ticketController);
}
