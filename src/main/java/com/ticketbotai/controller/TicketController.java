package com.ticketbotai.controller;

<<<<<<< HEAD
import com.ticketbotai.controller.dto.TicketRequest;
import com.ticketbotai.model.Ticket;
import com.ticketbotai.service.TicketService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets") // Rota base para todos os endpoints
public class TicketController {

    // Injeta o "cérebro" (Serviço)
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Endpoint POST /api/tickets
     * Cria um novo ticket.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna StatusCode 201 (Created)
    public Ticket createTicket(@RequestBody TicketRequest ticketRequest) {
        // Delega toda a lógica para o serviço
        return ticketService.createTicket(ticketRequest);
    }

    /**
     * Endpoint GET /api/tickets/{id}
     * Busca um ticket específico.
     */
    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        // Delega a lógica de busca para o serviço
        return ticketService.getTicketById(id);
    }
}
=======
public class TicketController {
    
}

>>>>>>> 0bf7e357bce491e09e9243ea95f9e7fc56634749
