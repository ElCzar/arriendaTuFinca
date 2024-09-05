package com.gossip.arrienda_tu_finca.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TenantRentalRequestDTO {
    private Long id;
	private Long propertyId;
	private String requesterEmail;
	private LocalDateTime requestDateTime;
	private LocalDate arrivalDate;
	private LocalDate departureDate;
	private Integer peopleNumber;
	private Double amount;
	private boolean accepted;
	private boolean rejected;
	private boolean canceled;
	private boolean paid;
	private boolean landlordReviewed;
    private boolean propertyReviewed;
	private boolean completed;
	private boolean approved;
}
