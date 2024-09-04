package com.gossip.arrienda_tu_finca.dto;

public class PaymentDTO {

    private Long id;
    private Long property_id;
    private String tenant_email;
    private Double rentalPrice;
    private String bank;
    private Integer accountNumber;

    // Constructor
    public PaymentDTO(Long id, Long property_id, String tenant_email, Double rentalPrice, String bank, Integer accountNumber) {
        this.id = id;
        this.property_id = property_id;
        this.tenant_email = tenant_email;
        this.rentalPrice = rentalPrice;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getProperty_id() {
        return property_id;
    }

    public String getTenant_email() {
        return tenant_email;
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

    public void setProperty_id(Long property_id) {
        this.property_id = property_id;
    }

    public void setTenant_email(String tenant_email) {
        this.tenant_email = tenant_email;
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
