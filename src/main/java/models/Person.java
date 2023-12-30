package models;


import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Person implements Model {
    private final Long id;
    // A map containing to who the person owes a debt
    // If debt < 0 --> this person needs to receive from the one in the map
    // If debt > 0 --> this person needs to pay to the on in the map
    private final Map<Long, Double> debts = new HashMap<>();
    private final List<Long> ticketsId = new ArrayList<>();
    private String name;

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
