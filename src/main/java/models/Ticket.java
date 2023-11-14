package models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Ticket {
    private final long id = System.nanoTime();
    private double cost;
    private Long ticketCategoryId;
    private Map<Long, Double> distribution = new HashMap<>();
}
