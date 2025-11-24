package com.monsterdam.admin.domain.enumeration;

/**
 * The TicketStatus enumeration.
 */
public enum TicketStatus {
    OPEN("admin.ticket.status.open"),
    ASSIGNED("admin.ticket.status.assigned"),
    CLOSED("admin.ticket.status.closed");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
