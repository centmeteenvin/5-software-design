package models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Ticket implements Model {
    private final Long id = System.nanoTime();
    private double cost;
    private Long ticketCategoryId;
    private Map<Long, Double> distribution = new HashMap<>();
}
