package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TicketCategory implements Model {
    private final Long id = System.nanoTime();
    private String name;
    private final List<Long> ticketIds = new ArrayList<>();
}
