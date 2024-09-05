package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {
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
    private String ownerEmail;
}
