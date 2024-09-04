package com.gossip.arrienda_tu_finca.dto;

import java.time.LocalDate;

public class RequestARentalDTO {

    private Long id;
    private Long property_id;
    private String requester_email;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer peopleNumber;

    public RequestARentalDTO(Long id, Long property, String requester, LocalDate startDate, LocalDate endDate, Integer peopleNumber) {
        this.id = id;
        this.property_id = property;
        this.requester_email = requester;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peopleNumber = peopleNumber;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public long getProperty_id() {
        return property_id;
    }

    public String getRequester_email() {
        return requester_email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setProperty_id(Long property) {
        this.property_id = property;
    }

    public void setRequester_email(String requester) {
        this.requester_email = requester;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

}


