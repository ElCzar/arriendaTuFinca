package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
import com.gossip.arrienda_tu_finca.services.PropertyService;

@RestController
@RequestMapping("/property")
public class PropertyController {
    private PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

      // Buscar propiedad

      @GetMapping("/random-municipality") //Obtener propiedades de un municipio aleatorio
      public ResponseEntity<List<PropertyListDTO>> getPropertiesByRandomMunicipality() {
          List<PropertyListDTO> properties = propertyService.getPropertiesByRandomMunicipality();
          return ResponseEntity.ok(properties);
      }
  
      @GetMapping("/{name}")  // Obtener una propiedad por nombre
      public ResponseEntity<PropertyListDTO> getPropertyByName(@PathVariable String name) {
          PropertyListDTO propertyDTO = propertyService.getPropertyByName(name);
          return ResponseEntity.ok(propertyDTO);
      }
  
      @GetMapping("/{municipality}")  // Obtener una propiedad por municipio
      public ResponseEntity<PropertyListDTO> getPropertyByMunicipality(@PathVariable String municipality) {
          PropertyListDTO propertyDTO = propertyService.getPropertyByMunicipality(municipality);
          return ResponseEntity.ok(propertyDTO);
      }
  
      @GetMapping("/{peopleNumber}")  // Obtener una propiedad por cantidad de personas
      public ResponseEntity<PropertyListDTO> getPropertyByPeopleNumber(@PathVariable Integer peopleNumber) {
          PropertyListDTO propertyDTO = propertyService.getPropertyByPeopleNumber(peopleNumber);
          return ResponseEntity.ok(propertyDTO);
      }
}
