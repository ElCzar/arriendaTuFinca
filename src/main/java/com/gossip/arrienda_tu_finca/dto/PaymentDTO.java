package com.gossip.arrienda_tu_finca.dto;

public class PaymentDTO {

    private Long id;
    private Long rentalRequest_id;
    private Double rentalPrice;
    private String bank;
    private Integer accountNumber;

    // Constructor
    public PaymentDTO(Long id, Long rentalRequest_id, Double rentalPrice, String bank, Integer accountNumber) {
        this.id = id;
        this.rentalRequest_id = rentalRequest_id;
        this.rentalPrice = rentalPrice;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getRentalRequest_id() {
        return rentalRequest_id;
    }

    public Double getRentalPrice() {
        return rentalPrice;
    }

    public String getBank() {
        return bank;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRentalRequest_id(Long rentalRequest_id) {
        this.rentalRequest_id = rentalRequest_id;
    }

    public void setRentalPrice(Double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

}
