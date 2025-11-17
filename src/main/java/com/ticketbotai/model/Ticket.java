package com.ticketbotai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 200)
    private String title;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TicketCategory category;
    
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;
    
    private String assignedTeam;
    
    private Double confidenceScore;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Getters e Setters
    public Long getId() { return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }
    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }
    public TicketCategory getCategory() { 
        return category; 
    }
    public void setCategory(TicketCategory category) { 
        this.category = category; 
    }
    public TicketStatus getStatus() { 
        return status;
     }
    public void setStatus(TicketStatus status) { 
        this.status = status; 
    }
    public String getAssignedTeam() { 
        return assignedTeam; 
    }
    public void setAssignedTeam(String assignedTeam) { 
        this.assignedTeam = assignedTeam;
    }
    public Double getConfidenceScore() { 
        return confidenceScore; 
    }
    public void setConfidenceScore(Double confidenceScore) { 
        this.confidenceScore = confidenceScore; 
    }
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}
