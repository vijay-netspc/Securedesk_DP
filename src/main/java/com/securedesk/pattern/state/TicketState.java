package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

/**
 * STATE PATTERN
 * -------------
 * Each concrete state knows which transitions are legal from itself and
 * throws if an illegal transition is attempted, instead of the service
 * layer maintaining a big if/else or switch block of valid transitions.
 */
public interface TicketState {

    default void assign(TicketStateContext context) {
        throw new IllegalStateException("Cannot ASSIGN a ticket in state " + getStatus());
    }

    default void startProgress(TicketStateContext context) {
        throw new IllegalStateException("Cannot START PROGRESS on a ticket in state " + getStatus());
    }

    default void resolve(TicketStateContext context) {
        throw new IllegalStateException("Cannot RESOLVE a ticket in state " + getStatus());
    }

    default void close(TicketStateContext context) {
        throw new IllegalStateException("Cannot CLOSE a ticket in state " + getStatus());
    }

    TicketStatusEnum getStatus();
}
