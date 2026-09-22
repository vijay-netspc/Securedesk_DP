package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

public class OpenState implements TicketState {

    @Override
    public void assign(TicketStateContext context) {
        context.setState(new AssignedState());
    }

    @Override
    public TicketStatusEnum getStatus() { return TicketStatusEnum.OPEN; }
}
