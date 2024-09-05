package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private Double rentalPrice;
    private String bank;
    private Integer accountNumber;
    private Long rentalRequestId;
}
