package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

/** Terminal state - every transition method uses the interface's default
 * (throwing) behaviour, since a closed ticket cannot move anywhere. */
public class ClosedState implements TicketState {

    @Override
    public TicketStatusEnum getStatus() { return TicketStatusEnum.CLOSED; }
}
