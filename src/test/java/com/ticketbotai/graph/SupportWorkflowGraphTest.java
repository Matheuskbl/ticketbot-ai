package com.ticketbotai.graph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SupportWorkflowGraphTest {

    private final SupportWorkflowGraph graph = new SupportWorkflowGraph(); 

    @Test
    void testFindOptimalRoute_BugCritico() {
        String expectedDestination = "Equipe Segurança L3"; 
        String actualDestination = graph.findOptimalRoute("Bug Crítico");
        
        assertEquals(expectedDestination, actualDestination, "A rota para Bug Crítico deve levar à Segurança L3.");
    }

    @Test
    void testFindOptimalRoute_DuvidaCobranca() {
        String expectedDestination = "Equipe Financeiro";
        String actualDestination = graph.findOptimalRoute("Dúvida Cobrança");
        
        assertEquals(expectedDestination, actualDestination, "A rota para Cobrança deve levar ao Financeiro.");
    }

    @Test
    void testFindOptimalRoute_CategoriaInexistente() {
        String expectedMessage = "Destino não encontrado (Erro na Classificação)";
        String actualMessage = graph.findOptimalRoute("Categoria Invalida");
        
        assertEquals(expectedMessage, actualMessage, "Deve retornar mensagem de erro para categoria não mapeada.");
    }
}