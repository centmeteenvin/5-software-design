package models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Ticket implements Model {
    private final Long id;
    // How much everyone SHOULD pay
    // will always be positive!!
    private final Map<Long, Double> distribution = new HashMap<>();
    private double cost;
    private Long ticketCategoryId;
    private Long payerId;

    public Ticket(Long id, double cost, Long ticketCategoryId) {
        this.id = id;
        this.cost = cost;
        this.ticketCategoryId = ticketCategoryId;
    }

}
