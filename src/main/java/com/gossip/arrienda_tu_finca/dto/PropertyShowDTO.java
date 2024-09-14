package com.gossip.arrienda_tu_finca.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyShowDTO {

    @Lob // Para manejar objetos pesados en JPA
    private byte[] photo;

    private String name;
    private String description;
    private String municipality;
    private String link;
}
