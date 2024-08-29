package com.gossip.arrienda_tu_finca.entities;

import org.springframework.cglib.core.Local;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "rental_requests")
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 
    

    
    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User requester;

    private LocalDateTime requestDateTime;  
    private LocalDate arrivalDate; 
    private LocalDate departureDate;  
    private Double amount;  
    private boolean accepted;
    private boolean rejected;
    private boolean canceled;
    private boolean paid;
    private boolean reviewed;
    private boolean completed;
    private boolean approved;
    
}

