package com.gossip.arrienda_tu_finca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rentalPrice;
    private String bank;
    private Integer accountNumber;

    // OneToOne relationship with TenantRentalRequest
    @OneToOne
    @JoinColumn(name = "rentalRequest_id", referencedColumnName = "id")
    private TenantRentalRequest rentalRequest;
}
