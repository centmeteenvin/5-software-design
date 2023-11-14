package models;

import java.util.ArrayList;
import java.util.List;

public class TicketCategory {
    private final Long id = System.nanoTime();
    private String name;
    private final List<Long> ticketIds = new ArrayList<>();
}
