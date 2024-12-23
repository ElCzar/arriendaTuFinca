package com.gossip.arrienda_tu_finca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private Integer amountOfResidents;
    private Double amount;  
    private boolean approved;
    private boolean rejected;
    private boolean canceled;
    private boolean paid;
    private boolean completed;
    private boolean expired;
    private String bank;
    private Integer accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_comment_id", referencedColumnName = "id")
    private Comment hostComment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_comment_id", referencedColumnName = "id")
    private Comment renterComment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_comment_id", referencedColumnName = "id")
    private Comment propertyComment;
}