package com.gossip.arrienda_tu_finca.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalRequestCreateDTO {
    private String requesterEmail;  
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Integer amountOfResidents;
}
