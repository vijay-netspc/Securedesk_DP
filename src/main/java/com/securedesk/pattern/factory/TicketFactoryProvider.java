package com.securedesk.pattern.factory;

import com.securedesk.enums.TicketCategory;
import org.springframework.stereotype.Component;

/**
 * Simple Factory that hands out the correct TicketCreator (Factory Method)
 * implementation for a given category. This is the single point the
 * service layer talks to - it never needs to know which concrete
 * creator class exists for a category.
 */
@Component
public class TicketFactoryProvider {

    public TicketCreator getCreator(TicketCategory category) {
        return switch (category) {
            case HARDWARE -> new HardwareTicketCreator();
            case SOFTWARE -> new SoftwareTicketCreator();
            case NETWORK -> new NetworkTicketCreator();
            case SECURITY -> new SecurityTicketCreator();
            case ACCESS -> new AccessTicketCreator();
        };
    }
}
