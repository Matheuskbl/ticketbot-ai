package com.ticketbotai.controller;

import com.ticketbotai.controller.dto.TicketRequest;
import com.ticketbotai.model.Ticket;
import com.ticketbotai.service.TicketService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets") 
@CrossOrigin(origins = "*")
public class TicketController {

   
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) 
    public Ticket createTicket(@RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest);
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        // Delega a lógica de busca para o serviço
        return ticketService.getTicketById(id);
    }
}