package com.ticketbotai.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TextClassificationServiceTest {

    @Autowired
    private TextClassificationService textClassificationService;

    @Test
    void testClassifyHardwareFailure() {
        // Frase clara sobre hardware
        String text = "O meu monitor não liga e a tela está preta";
        String category = textClassificationService.classifyTicket(text);
        
        // Espera a categoria exata do .arff
        assertEquals("Falha Hardware", category, "O texto sobre hardware deve ser classificado corretamente.");
    }

    @Test
    void testClassifyBillingDoubt() {
        // Frase forte sobre cobrança
        String text = "Recebi uma fatura duplicada e quero saber o valor";
        String category = textClassificationService.classifyTicket(text);
        
        assertEquals("Dúvida Cobrança", category, "O texto sobre cobrança deve ser classificado corretamente.");
    }

    @Test
    void testClassifyCriticalBug() {
        // Frase forte sobre bug
        String text = "O sistema travou e deu erro 500 ao salvar";
        String category = textClassificationService.classifyTicket(text);
        
        assertEquals("Bug Crítico", category, "O texto sobre bug crítico deve ser classificado corretamente.");
    }
}