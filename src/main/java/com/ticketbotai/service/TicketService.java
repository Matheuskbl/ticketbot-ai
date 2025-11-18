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
import java.text.Normalizer; // Importante para remover acentos
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
        
        // 1. IA Classifica (Retorna ex: "Dúvida Cobrança" ou "Falha Hardware")
        String categoryNameRaw = aiService.classifyTicket(request.description());

        // 2. Grafo Roteia (Usa a string original, pois o grafo usa chaves com acento)
        String assignedTeam = workflowGraph.findOptimalRoute(categoryNameRaw);

        // 3. Conversão Blindada para Enum (Remove acentos e espaços)
        TicketCategory categoryEnum = mapToEnum(categoryNameRaw);

        // 4. Monta e Salva
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

    // --- MÉTODO AUXILIAR PARA "TRADUZIR" A STRING DA IA ---
    private TicketCategory mapToEnum(String rawCategory) {
        try {
            // 1. Normaliza (separa os acentos das letras)
            String normalized = Normalizer.normalize(rawCategory, Normalizer.Form.NFD);
            // 2. Remove os acentos usando Regex
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String noAccent = pattern.matcher(normalized).replaceAll("");
            
            // 3. Transforma em formato de Enum (Maiúsculas e com Underline)
            // Ex: "Duvida Cobranca" -> "DUVIDA_COBRANCA"
            String enumKey = noAccent.toUpperCase().replace(" ", "_");
            
            return TicketCategory.valueOf(enumKey);
        } catch (Exception e) {
            // Se mesmo assim falhar, ou se a IA devolver um erro, salvamos como OUTROS
            // para não quebrar a aplicação com erro 500.
            System.err.println("Falha ao converter categoria: " + rawCategory);
            return TicketCategory.OUTROS;
        }
    }
}