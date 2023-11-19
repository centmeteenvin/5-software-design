package models;


import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Person implements Model {
 private final Long id;
 private String name;
 private final Map<Long, Double> debts = new HashMap<>();
 private final List<Long> ticketsId = new ArrayList<>();

 public Person(Long id, String name) {
  this.id = id;
  this.name = name;
 }
}
