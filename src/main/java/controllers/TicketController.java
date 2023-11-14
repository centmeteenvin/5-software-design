package controllers;

import models.Ticket;

import java.util.List;

public interface TicketController {
    Ticket create(Long categoryId, double totalCost, List<Long> personsId);
    void addPerson(Long id, Long personId);
    void removePerson(Long id, Long personId);
    void changeCategory(Long id, Long newCategoryId);
    void changeCost(Long id, double newTotalCost);
    void changeWeight(Long id, Long personId, double newWeight);
    void delete(Long id);
    void calculate(Long id);

    void setPersonController(PersonController personController);
    void setTicketCategoryController(TicketCategoryController ticketCategoryController);
}
