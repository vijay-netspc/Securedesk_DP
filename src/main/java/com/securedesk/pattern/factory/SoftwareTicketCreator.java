package com.securedesk.pattern.factory;

import com.securedesk.enums.SupportLevel;
import com.securedesk.enums.TicketCategory;

public class SoftwareTicketCreator extends TicketCreator {
    @Override
    protected TicketCategory getCategory() { return TicketCategory.SOFTWARE; }

    @Override
    protected SupportLevel getInitialSupportLevel() { return SupportLevel.LEVEL1; }
}
