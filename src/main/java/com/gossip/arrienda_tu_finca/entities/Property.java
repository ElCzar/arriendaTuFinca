package com.gossip.arrienda_tu_finca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Lob
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "owner_email", referencedColumnName = "email")
    private User owner;
}
