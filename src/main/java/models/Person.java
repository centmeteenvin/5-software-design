package models;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Person implements Model {
 private final Long id;
 private String name;
 private double debt = 0;
 private final List<Long> ticketsId = new ArrayList<>();

 public Person(Long id, String name, double debt) {
  this.id = id;
  this.name = name;
  this.debt = debt;
 }
}
