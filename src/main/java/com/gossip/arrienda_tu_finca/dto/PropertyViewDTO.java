package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyViewDTO {
    private Long id;
    private String name;
    private String description;
    private String municipality;
    private String typeOfEntrance;
    private String address;
    private boolean isAvailable;
    private Double pricePerNight;
    private int amountOfRooms;
    private int amountOfBathrooms;
    private boolean isPetFriendly;
    private boolean hasPool;
    private boolean hasGril;
}
