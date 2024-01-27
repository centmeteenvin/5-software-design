package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TicketCategory implements Model {
    private final Long id;
    private final List<Long> ticketIds = new ArrayList<>();
    private String name;

    public TicketCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
