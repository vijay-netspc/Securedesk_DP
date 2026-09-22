package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

/**
 * Context object used by the Service layer to drive a ticket through its
 * lifecycle. It is instantiated with the ticket's current status (read
 * from the database), reconstructs the matching State object, and any
 * transition call is delegated to that object. After a legal transition
 * the new TicketStatusEnum is read back out and persisted.
 */
public class TicketStateContext {

    private TicketState currentState;

    public TicketStateContext(TicketStatusEnum status) {
        this.currentState = fromStatus(status);
    }

    public void setState(TicketState state) {
        this.currentState = state;
    }

    public void assign() { currentState.assign(this); }
    public void startProgress() { currentState.startProgress(this); }
    public void resolve() { currentState.resolve(this); }
    public void close() { currentState.close(this); }

    public TicketStatusEnum getCurrentStatus() {
        return currentState.getStatus();
    }

    private static TicketState fromStatus(TicketStatusEnum status) {
        return switch (status) {
            case OPEN -> new OpenState();
            case ASSIGNED -> new AssignedState();
            case IN_PROGRESS -> new InProgressState();
            case RESOLVED -> new ResolvedState();
            case CLOSED -> new ClosedState();
        };
    }
}
