package com.ticketbotai.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TextClassificationServiceTest {

    @Autowired
    private TextClassificationService classificationService;

    @Test
    void testClassifyHardwareFailure() {
        String expectedCategory = "Falha de Hardware";
        String actualCategory = classificationService.classifyTicket("meu mouse parou de funcionar e a tela quebrou");
        
        assertEquals(expectedCategory, actualCategory, "O texto sobre hardware deve ser classificado corretamente.");
    }

    @Test
    void testClassifyBillingDoubt() {
        String expectedCategory = "Dúvida Cobrança";
        String actualCategory = classificationService.classifyTicket("nao recebi o boleto esse mes");
        
        assertEquals(expectedCategory, actualCategory, "O texto sobre cobrança deve ser classificado corretamente.");
    }

    @Test
    void testClassifyCriticalBug() {
        String expectedCategory = "Bug Crítico";
        String actualCategory = classificationService.classifyTicket("o sistema deu erro 404 e nao consigo logar");
        
        assertEquals(expectedCategory, actualCategory, "O texto sobre bug crítico deve ser classificado corretamente.");
    }
}