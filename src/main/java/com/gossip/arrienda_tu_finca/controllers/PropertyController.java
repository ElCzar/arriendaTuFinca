package com.gossip.arrienda_tu_finca.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
import com.gossip.arrienda_tu_finca.services.PropertyService;

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

import com.gossip.arrienda_tu_finca.dto.PropertyCreateDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyUpdateDTO;
import com.gossip.arrienda_tu_finca.services.PropertyService;

import jakarta.validation.Valid;

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
  
    // Obtener una propiedad por ID
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable Long id) {
        PropertyDTO propertyDTO = propertyService.getPropertyById(id);
        return ResponseEntity.ok(propertyDTO);
    }

    // Obtener todas las propiedades
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Crear una propiedad (Usa PropertyCreateDTO)
    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyCreateDTO propertyCreateDTO) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyCreateDTO);
        return ResponseEntity.ok(createdProperty);
    }

    // Actualizar una propiedad (Usa PropertyUpdateDTO)
    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody PropertyUpdateDTO propertyUpdateDTO) {
        PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyUpdateDTO);
        return ResponseEntity.ok(updatedProperty);
    }

    // Desactivar una propiedad (Cambiar su estado a no disponible)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateProperty(@PathVariable Long id) {
        propertyService.deactivateProperty(id); // Desactiva la propiedad en lugar de eliminarla
        return ResponseEntity.noContent().build(); 
    }

    // Subir una foto para una propiedad
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<Void> uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            propertyService.uploadPhoto(id, photo);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
  
}
