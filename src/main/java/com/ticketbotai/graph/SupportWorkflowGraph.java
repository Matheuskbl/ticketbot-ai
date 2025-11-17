package com.ticketbotai.graph;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SupportWorkflowGraph {

    private final Map<String, List<String>> adjList;

    public SupportWorkflowGraph() {
        this.adjList = new HashMap<>();
        initializeGraph();
    }

    private void addEdge(String source, String destination) {
        adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
    }

    private void initializeGraph() {
        addEdge("Bug Crítico", "Equipe Desenvolvimento L1");
        addEdge("Bug Crítico", "Equipe Infraestrutura"); 
        addEdge("Dúvida Cobrança", "Equipe Financeiro");
        addEdge("Falha Hardware", "Equipe Infraestrutura");
        addEdge("Equipe Desenvolvimento L1", "Equipe DevOps L2"); 
        addEdge("Equipe Infraestrutura", "Equipe Segurança L3");
    }

    public String findOptimalRoute(String startNode) {
        if (!adjList.containsKey(startNode)) {
            return "Destino não encontrado (Erro na Classificação)";
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);
        String finalDestination = startNode;

        while (!queue.isEmpty()) {
            String current = queue.poll();
            finalDestination = current; 

            List<String> neighbors = adjList.getOrDefault(current, Collections.emptyList());
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return finalDestination;
    }
}