package com.gossip.arrienda_tu_finca.dto;

import java.time.LocalDate;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalRequestCreateDTO {

    @NotBlank(message = "Requester email is required")
    private String requesterEmail;  

    @NotBlank(message = "Start date is required")
    private LocalDate startDate;

    @NotBlank(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Amount of residents is required")
    @Positive(message = "Amount of residents must be positive")
    private Integer amountOfResidents;
}
