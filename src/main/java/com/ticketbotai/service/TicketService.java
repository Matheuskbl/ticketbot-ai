package com.ticketbotai.service;

import com.ticketbotai.controller.dto.TicketRequest;
import com.ticketbotai.model.Ticket;
import com.ticketbotai.model.TicketCategory; // Importe o seu Enum
import com.ticketbotai.repository.TicketRepository;
import com.ticketbotai.ai.TextClassificationService; // Pilar IA
import com.ticketbotai.graph.SupportWorkflowGraph;  // Pilar Grafo

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TextClassificationService aiService;
    private final SupportWorkflowGraph workflowGraph;

    public TicketService(TicketRepository ticketRepository,
                         TextClassificationService aiService,
                         SupportWorkflowGraph workflowGraph) {
        this.ticketRepository = ticketRepository;
        this.aiService = aiService;
        this.workflowGraph = workflowGraph;
    }

    /**
     * Lógica para criar um novo ticket (POST)
     */
    public Ticket createTicket(TicketRequest request) {
        
        // 1. Pilar IA: (CORRIGIDO)
        // Usa o método correto 'classifyTicket'. Retorna uma String, ex: "Falha Hardware"
        String categoryName = aiService.classifyTicket(request.description());

        // 2. Pilar Grafo: (CORRIGIDO)
        // Usa o método correto 'findOptimalRoute' e a String da IA como nó de início.
        String assignedTeam = workflowGraph.findOptimalRoute(categoryName);

        // 3. Conversão para Enum (para salvar no BD)
        // Converte a String (ex: "Falha Hardware") para o formato Enum (ex: FALHA_HARDWARE)
        TicketCategory categoryEnum;
        try {
            // Converte "Falha Hardware" -> "FALHA_HARDWARE"
            String enumName = categoryName.toUpperCase().replace(" ", "_");
            categoryEnum = TicketCategory.valueOf(enumName);
        } catch (Exception e) {
            // Se a conversão falhar (ex: a String da IA não bate com o Enum)
            throw new IllegalArgumentException("Categoria da IA '" + categoryName + "' não corresponde a um Enum TicketCategory válido.");
        }

        // 4. Monta a Entidade
        Ticket newTicket = new Ticket();
        newTicket.setTitle(request.title());
        newTicket.setDescription(request.description());
        newTicket.setCategory(categoryEnum);      // Salva o Enum
        newTicket.setAssignedTeam(assignedTeam); // Salva a String da Equipa

        // 5. Pilar Persistência (JPA): Salva no banco de dados
        return ticketRepository.save(newTicket);
    }

    /**
     * Lógica para ler um ticket (GET)
     */
    public Ticket getTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Ticket com ID " + id + " não encontrado."
            );
        }
    }
}