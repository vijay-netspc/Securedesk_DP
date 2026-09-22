package com.securedesk.controller;

import com.securedesk.dto.AssignTicketDto;
import com.securedesk.dto.EscalationHistoryDto;
import com.securedesk.dto.TicketRequestDto;
import com.securedesk.dto.TicketResponseDto;
import com.securedesk.dto.TicketStatusHistoryDto;
import com.securedesk.entity.Ticket;
import com.securedesk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketRequestDto dto) {
        Ticket ticket = ticketService.createTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponseDto.from(ticket));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getAll() {
        List<TicketResponseDto> tickets = ticketService.getAll().stream()
                .map(TicketResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(TicketResponseDto.from(ticketService.getById(id)));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<TicketResponseDto> assign(@PathVariable Long id, @Valid @RequestBody AssignTicketDto dto) {
        Ticket ticket = ticketService.assignTicket(id, dto.getStaffUserId());
        return ResponseEntity.ok(TicketResponseDto.from(ticket));
    }

    @PutMapping("/{id}/start-progress")
    public ResponseEntity<TicketResponseDto> startProgress(@PathVariable Long id) {
        Ticket ticket = ticketService.startProgress(id);
        return ResponseEntity.ok(TicketResponseDto.from(ticket));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<TicketResponseDto> resolve(@PathVariable Long id) {
        Ticket ticket = ticketService.resolveTicket(id);
        return ResponseEntity.ok(TicketResponseDto.from(ticket));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<TicketResponseDto> close(@PathVariable Long id) {
        Ticket ticket = ticketService.closeTicket(id);
        return ResponseEntity.ok(TicketResponseDto.from(ticket));
    }

    @PostMapping("/{id}/escalate")
    public ResponseEntity<TicketResponseDto> escalate(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String reason = (body != null && body.get("reason") != null) ? body.get("reason") : "Not resolved at current level";
        Ticket ticket = ticketService.escalateTicket(id, reason);
        return ResponseEntity.ok(TicketResponseDto.from(ticket));
    }

    @GetMapping("/{id}/status-history")
    public ResponseEntity<List<TicketStatusHistoryDto>> statusHistory(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getStatusHistory(id));
    }

    @GetMapping("/{id}/escalation-history")
    public ResponseEntity<List<EscalationHistoryDto>> escalationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getEscalationHistory(id));
    }
}
