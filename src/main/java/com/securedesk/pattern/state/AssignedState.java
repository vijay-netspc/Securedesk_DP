package com.securedesk.pattern.state;

import com.securedesk.enums.TicketStatusEnum;

public class AssignedState implements TicketState {

    @Override
    public void startProgress(TicketStateContext context) {
        context.setState(new InProgressState());
    }

    @Override
    public TicketStatusEnum getStatus() { return TicketStatusEnum.ASSIGNED; }
}
