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
public class RequestARentalDTO {

    private Long id;
    private Long propertyId;
    private String requesterEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer peopleNumber;
}


