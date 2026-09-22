package com.securedesk.pattern.factory;

import com.securedesk.enums.SupportLevel;
import com.securedesk.enums.TicketCategory;

public class SecurityTicketCreator extends TicketCreator {
    @Override
    protected TicketCategory getCategory() { return TicketCategory.SECURITY; }

    @Override
    protected SupportLevel getInitialSupportLevel() { return SupportLevel.SECURITY_SPECIALIST; }
}
