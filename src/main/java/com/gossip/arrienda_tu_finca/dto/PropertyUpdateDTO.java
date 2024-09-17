package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyUpdateDTO {
    private String name;
    private String description;
    private String municipality;
    private String department;
    private String typeOfEntrance;
    private String address;
    private String link;
    private Double pricePerNight;
    private int amountOfRooms;
    private int amountOfBathrooms;
    private int amountOfResidents;
    private boolean isPetFriendly;
    private boolean hasPool;
    private boolean hasGril;
}
