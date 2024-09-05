package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyListDTO {

    private Long id;
    private String name;
    private String municipality;
    private String department;
    private Integer peopleNumber;
    private String propertyLink;
    private byte[] photos;
    private String ownerEmail;
}
