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

import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.services.PropertyService;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

      // Buscar propiedad

      @GetMapping("/random-municipality") //Obtener propiedades de un municipio aleatorio
      public ResponseEntity<List<PropertyDTO>> getPropertiesByRandomMunicipality() {
          List<PropertyDTO> properties = propertyService.getPropertiesByRandomMunicipality();
          return ResponseEntity.ok(properties);
      }
  
      @GetMapping("/{name}")  // Obtener una propiedad por nombre
      public ResponseEntity<PropertyDTO> getPropertyByName(@PathVariable String name) {
          PropertyDTO propertyDTO = propertyService.getPropertyByName(name);
          return ResponseEntity.ok(propertyDTO);
      }
  
      @GetMapping("/{municipality}")  // Obtener una propiedad por municipio
      public ResponseEntity<PropertyDTO> getPropertyByMunicipality(@PathVariable String municipality) {
          PropertyDTO propertyDTO = propertyService.getPropertyByMunicipality(municipality);
          return ResponseEntity.ok(propertyDTO);
      }
  
      @GetMapping("/{peopleNumber}")  // Obtener una propiedad por cantidad de personas
      public ResponseEntity<PropertyDTO> getPropertyByPeopleNumber(@PathVariable Integer peopleNumber) {
          PropertyDTO propertyDTO = propertyService.getPropertyByPeopleNumber(peopleNumber);
          return ResponseEntity.ok(propertyDTO);
      }
}
