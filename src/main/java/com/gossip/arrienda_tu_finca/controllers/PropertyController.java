package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
import com.gossip.arrienda_tu_finca.services.PropertyService;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

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
