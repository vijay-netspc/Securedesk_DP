package com.securedesk.pattern.factory;

import com.securedesk.enums.SupportLevel;
import com.securedesk.enums.TicketCategory;

public class NetworkTicketCreator extends TicketCreator {
    @Override
    protected TicketCategory getCategory() { return TicketCategory.NETWORK; }

    @Override
    protected SupportLevel getInitialSupportLevel() { return SupportLevel.LEVEL2; }
}
