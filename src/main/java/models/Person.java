package models;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Person implements Model {
 private final Long id = System.nanoTime();
 private String name;
 private double debt = 0;
 private final List<Long> ticketsId = new ArrayList<>();
}
