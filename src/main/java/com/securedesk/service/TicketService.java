package com.securedesk.service;

import com.securedesk.dto.EscalationHistoryDto;
import com.securedesk.dto.TicketRequestDto;
import com.securedesk.dto.TicketStatusHistoryDto;
import com.securedesk.entity.Ticket;
import com.securedesk.entity.TicketStatusHistory;
import com.securedesk.entity.User;
import com.securedesk.enums.TicketStatusEnum;
import com.securedesk.pattern.chain.EscalationChainBuilder;
import com.securedesk.pattern.chain.SupportHandler;
import com.securedesk.pattern.factory.TicketCreator;
import com.securedesk.pattern.factory.TicketFactoryProvider;
import com.securedesk.pattern.observer.TicketNotifier;
import com.securedesk.pattern.state.TicketStateContext;
import com.securedesk.pattern.strategy.PriorityContext;
import com.securedesk.repository.EscalationHistoryRepository;
import com.securedesk.repository.TicketRepository;
import com.securedesk.repository.TicketStatusHistoryRepository;
import com.securedesk.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketStatusHistoryRepository statusHistoryRepository;
    private final EscalationHistoryRepository escalationHistoryRepository;
    private final TicketFactoryProvider factoryProvider;   // FACTORY
    private final PriorityContext priorityContext;         // STRATEGY
    private final TicketNotifier ticketNotifier;           // OBSERVER
    private final EscalationChainBuilder escalationChainBuilder; // CHAIN OF RESPONSIBILITY

    public TicketService(TicketRepository ticketRepository,
                          UserRepository userRepository,
                          TicketStatusHistoryRepository statusHistoryRepository,
                          EscalationHistoryRepository escalationHistoryRepository,
                          TicketFactoryProvider factoryProvider,
                          PriorityContext priorityContext,
                          TicketNotifier ticketNotifier,
                          EscalationChainBuilder escalationChainBuilder) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.escalationHistoryRepository = escalationHistoryRepository;
        this.factoryProvider = factoryProvider;
        this.priorityContext = priorityContext;
        this.ticketNotifier = ticketNotifier;
        this.escalationChainBuilder = escalationChainBuilder;
    }

    public Ticket createTicket(TicketRequestDto dto) {
        User createdBy = userRepository.findById(dto.getCreatedByUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getCreatedByUserId()));

        // ---- FACTORY PATTERN: build the right kind of ticket for the category ----
        TicketCreator creator = factoryProvider.getCreator(dto.getCategory());
        Ticket ticket = creator.createTicket(dto.getTitle(), dto.getDescription(), createdBy);

        // ---- STRATEGY PATTERN: calculate priority using the category-appropriate algorithm ----
        ticket.setPriority(priorityContext.calculate(ticket));

        Ticket saved = ticketRepository.save(ticket);

        ticketNotifier.notifyObservers(saved, "CREATED",
                "Ticket #" + saved.getId() + " (" + saved.getCategory() + ") created with priority " + saved.getPriority());

        return saved;
    }

    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    public Ticket getById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id " + id));
    }

    public Ticket assignTicket(Long ticketId, Long staffUserId) {
        Ticket ticket = getById(ticketId);
        User staff = userRepository.findById(staffUserId)
                .orElseThrow(() -> new IllegalArgumentException("Staff user not found: " + staffUserId));

        // ---- STATE PATTERN: only a valid transition (OPEN -> ASSIGNED) is allowed ----
        transition(ticket, TicketStateContext::assign);

        ticket.setAssignedTo(staff);
        Ticket saved = ticketRepository.save(ticket);

        // ---- OBSERVER PATTERN: notify assigned staff ----
        ticketNotifier.notifyObservers(saved, "ASSIGNED",
                "Ticket #" + saved.getId() + " has been assigned to you.");

        return saved;
    }

    public Ticket startProgress(Long ticketId) {
        Ticket ticket = getById(ticketId);
        transition(ticket, TicketStateContext::startProgress);
        Ticket saved = ticketRepository.save(ticket);
        ticketNotifier.notifyObservers(saved, "IN_PROGRESS",
                "Work has started on ticket #" + saved.getId());
        return saved;
    }

    public Ticket resolveTicket(Long ticketId) {
        Ticket ticket = getById(ticketId);
        transition(ticket, TicketStateContext::resolve);
        Ticket saved = ticketRepository.save(ticket);
        ticketNotifier.notifyObservers(saved, "RESOLVED",
                "Your ticket #" + saved.getId() + " has been resolved.");
        return saved;
    }

    public Ticket closeTicket(Long ticketId) {
        Ticket ticket = getById(ticketId);
        transition(ticket, TicketStateContext::close);
        Ticket saved = ticketRepository.save(ticket);
        ticketNotifier.notifyObservers(saved, "CLOSED",
                "Ticket #" + saved.getId() + " has been closed.");
        return saved;
    }

    public Ticket escalateTicket(Long ticketId, String reason) {
        Ticket ticket = getById(ticketId);

        if (ticket.getCurrentSupportLevel() == com.securedesk.enums.SupportLevel.UNRESOLVED) {
            throw new IllegalStateException("Ticket has already been escalated through the entire support chain.");
        }

        // ---- CHAIN OF RESPONSIBILITY PATTERN ----
        // Escalating means "the current tier could not resolve this, move it to the
        // next tier in Level1 -> Level2 -> NetworkSpecialist -> SecuritySpecialist".
        // We locate the handler matching the ticket's current tier, then hand it to
        // the *next* handler in the chain (recording that hop), rather than
        // restarting the canHandle check from Level 1 every time - otherwise a
        // routine ticket that already satisfies Level 1's own criteria would just
        // be re-confirmed at Level 1 and never actually move or leave an audit row.
        SupportHandler chainHead = escalationChainBuilder.buildChain();
        SupportHandler currentNode = findHandlerForLevel(chainHead, ticket.getCurrentSupportLevel());
        SupportHandler nextNode = (currentNode != null) ? currentNode.nextInChain() : chainHead;

        if (nextNode == null) {
            ticket.setCurrentSupportLevel(com.securedesk.enums.SupportLevel.UNRESOLVED);
        } else {
            com.securedesk.enums.SupportLevel fromLevel = ticket.getCurrentSupportLevel();
            escalationHistoryRepository.save(new com.securedesk.entity.EscalationHistory(ticket, fromLevel, nextNode.level(), reason));
            ticket.setCurrentSupportLevel(nextNode.level());
            // Let the new tier's own handle() run too, in case it also cannot own the
            // ticket (e.g. category mismatch) and needs to cascade further forward.
            nextNode.handle(ticket, reason);
        }

        Ticket saved = ticketRepository.save(ticket);

        ticketNotifier.notifyObservers(saved, "ESCALATED",
                "Ticket #" + saved.getId() + " escalated. Now routed to " + saved.getCurrentSupportLevel());

        return saved;
    }

    /** Walks the Chain of Responsibility to find the handler matching a given support level. */
    private SupportHandler findHandlerForLevel(SupportHandler head, com.securedesk.enums.SupportLevel level) {
        SupportHandler node = head;
        while (node != null) {
            if (node.level() == level) return node;
            node = node.nextInChain();
        }
        return null;
    }

    public List<TicketStatusHistoryDto> getStatusHistory(Long ticketId) {
        return statusHistoryRepository.findByTicketIdOrderByTimestampAsc(ticketId).stream()
                .map(TicketStatusHistoryDto::from)
                .collect(Collectors.toList());
    }

    public List<EscalationHistoryDto> getEscalationHistory(Long ticketId) {
        return escalationHistoryRepository.findByTicketIdOrderByTimestampAsc(ticketId).stream()
                .map(EscalationHistoryDto::from)
                .collect(Collectors.toList());
    }

    /** Runs a STATE PATTERN transition and persists a status-history audit row. */
    private void transition(Ticket ticket, java.util.function.Consumer<TicketStateContext> transitionFn) {
        TicketStatusEnum before = ticket.getStatus();
        TicketStateContext stateContext = new TicketStateContext(before);

        transitionFn.accept(stateContext); // throws IllegalStateException if the move is not legal

        TicketStatusEnum after = stateContext.getCurrentStatus();
        ticket.setStatus(after);
        statusHistoryRepository.save(new TicketStatusHistory(ticket, before, after));
    }
}
