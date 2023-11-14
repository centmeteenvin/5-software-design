package controllers;

import models.TicketCategory;

public interface TicketCategoryController {
    TicketCategory create(String Name);
    void addTicket(Long id, Long ticketId);
    void removeTicket(Long id, Long ticketId);
    void rename(Long id, String newName);
    void delete(Long id);
}
