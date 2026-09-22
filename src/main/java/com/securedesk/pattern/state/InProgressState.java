package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

public class InProgressState implements TicketState {

    @Override
    public void resolve(TicketStateContext context) {
        context.setState(new ResolvedState());
    }

    @Override
    public TicketStatusEnum getStatus() { return TicketStatusEnum.IN_PROGRESS; }
}
