package com.ticketbotai.controller.dto;

// Usamos um 'record' Java moderno para um DTO simples.
// Ele só precisa dos campos que o utilizador envia.
public record TicketRequest(
    String title,
    String description
) {
}