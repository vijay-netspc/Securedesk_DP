package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

public class ResolvedState implements TicketState {

    @Override
    public void close(TicketStateContext context) {
        context.setState(new ClosedState());
    }

    @Override
    public TicketStatusEnum getStatus() { return TicketStatusEnum.RESOLVED; }
}
