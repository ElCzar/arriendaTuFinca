package com.gossip.arrienda_tu_finca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Municipality is required")
    private String municipality;

    @NotBlank(message = "Type of entrance is required")
    private String typeOfEntrance;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price per night must be positive")
    private Double pricePerNight;

    @NotNull(message = "Amount of rooms is required")
    @Positive(message = "Amount of rooms must be positive")
    private Integer amountOfRooms;

    @NotNull(message = "Amount of bathrooms is required")
    @Positive(message = "Amount of bathrooms must be positive")
    private Integer amountOfBathrooms;

    @NotNull(message = "Pet friendly status is required")
    private Boolean isPetFriendly;

    @NotNull(message = "Pool status is required")
    private Boolean hasPool;

    @NotNull(message = "Grill status is required")
    private Boolean hasGril;

    @NotBlank(message = "Owner email is required")
    private String ownerEmail;
}
