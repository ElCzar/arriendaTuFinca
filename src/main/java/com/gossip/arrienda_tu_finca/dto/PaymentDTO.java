package com.gossip.arrienda_tu_finca.dto;

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
public class PaymentDTO {

    @NotNull(message = "Amount of residents is required")
    @Positive(message = "Amount of residents must be positive")
    private Integer amountOfResidents;
    
    @NotBlank(message = "Bank is required")
    private String bank;

    @NotNull(message = "Account number is required")
    @Positive(message = "Account number must be positive")
    private Integer accountNumber;
}
