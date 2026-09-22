package com.securedesk.pattern.factory;

import com.securedesk.enums.SupportLevel;
import com.securedesk.enums.TicketCategory;

public class AccessTicketCreator extends TicketCreator {
    @Override
    protected TicketCategory getCategory() { return TicketCategory.ACCESS; }

    @Override
    protected SupportLevel getInitialSupportLevel() { return SupportLevel.LEVEL1; }
}
