package com.gossip.arrienda_tu_finca.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RentalRequestViewDTO {

    private String propertyName;
    private LocalDateTime requestDateTime;  
    private LocalDate arrivalDate; 
    private LocalDate departureDate; 
    private Double amount;  
    private boolean status;

}
