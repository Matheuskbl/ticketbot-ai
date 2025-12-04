package com.ticketbotai.service;

import com.ticketbotai.controller.dto.TicketRequest;
import com.ticketbotai.model.Ticket;
import com.ticketbotai.model.TicketCategory;
import com.ticketbotai.repository.TicketRepository;
import com.ticketbotai.ai.TextClassificationService;
import com.ticketbotai.graph.SupportWorkflowGraph;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.text.Normalizer; 
import java.util.Optional;
import java.util.regex.Pattern;

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

    public Ticket createTicket(TicketRequest request) {

        String categoryNameRaw = aiService.classifyTicket(request.description());

        String assignedTeam = workflowGraph.findOptimalRoute(categoryNameRaw);

        TicketCategory categoryEnum = mapToEnum(categoryNameRaw);

        Ticket newTicket = new Ticket();
        newTicket.setTitle(request.title());
        newTicket.setDescription(request.description());
        newTicket.setCategory(categoryEnum);
        newTicket.setAssignedTeam(assignedTeam);

        return ticketRepository.save(newTicket);
    }

    public Ticket getTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado.");
        }
    }
    private TicketCategory mapToEnum(String rawCategory) {
        try {
            String normalized = Normalizer.normalize(rawCategory, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String noAccent = pattern.matcher(normalized).replaceAll("");
            String enumKey = noAccent.toUpperCase().replace(" ", "_");
            
            return TicketCategory.valueOf(enumKey);
        } catch (Exception e) {
            System.err.println("Falha ao converter categoria: " + rawCategory);
            return TicketCategory.OUTROS;
        }
    }
}