package com.gossip.arrienda_tu_finca.controllers;

import java.io.IOException;
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

    /**
     * Obtains the information of a property with a given id
     * @param id
     * @return PropertyDTO with the information of the property as a JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable Long id) {
        PropertyDTO propertyDTO = propertyService.getPropertyById(id);
        return ResponseEntity.ok(propertyDTO);
    }

    /**
     * Obtains the information of all properties
     * @return List<PropertyDTO> with the information of all properties as a JSON
     */
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    /**
     * Creates a new property
     * @param propertyCreateDTO
     * @return PropertyDTO with the information of the created property as a JSON
     */
    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyCreateDTO propertyCreateDTO) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyCreateDTO);
        return ResponseEntity.ok(createdProperty);
    }

    /**
     * Updates the information of a property with a given id
     * @param id
     * @param propertyUpdateDTO
     * @return PropertyDTO with the information of the updated property as a JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody PropertyUpdateDTO propertyUpdateDTO) {
        PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyUpdateDTO);
        return ResponseEntity.ok(updatedProperty);
    }

    /**
     * Deactivates a property with a given id
     * @param id
     * @return ResponseEntity<Void> with status 200
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateProperty(@PathVariable Long id) {
        propertyService.deactivateProperty(id); // Desactiva la propiedad en lugar de eliminarla
        return ResponseEntity.ok().build();
    }

    /**
     * Uploads a photo to a property with a given a property id
     * @param id
     * @param photo
     * @return
     */
    @PostMapping("/uploadPhoto/{id}")
    public ResponseEntity<Void> uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            propertyService.uploadPhoto(id, photo);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Obtains the information of all properties with a given name
     * @return List<PropertyDTO> with the information of all properties with a given name as a JSON
     */
    @GetMapping("/random-municipality")
    public ResponseEntity<List<PropertyDTO>> findPropertiesByRandomMunicipality() {
        List<PropertyDTO> properties = propertyService.findPropertiesByRandomMunicipality();
        return ResponseEntity.ok(properties);
    }

    /**
     * Obtains the information of all properties with a given name
     * @param name
     * @return List<PropertyDTO> with the information of all properties with a given name as a JSON
     */
    @GetMapping("/name")
    public ResponseEntity<List<PropertyDTO>> findPropertiesByName(@RequestBody String name) {
        List<PropertyDTO> properties = propertyService.findPropertiesByName(name);
        return ResponseEntity.ok(properties);
    }

    /**
     * Obtains the information of all properties with a given municipality
     * @param municipality
     * @return List<PropertyDTO> with the information of all properties with a given municipality as a JSON
     */
    @GetMapping("/municipality/{municipality}")
    public ResponseEntity<List<PropertyDTO>> findPropertiesByMunicipality(@PathVariable String municipality) {
        List<PropertyDTO> properties = propertyService.findPropertiesByMunicipality(municipality);
        return ResponseEntity.ok(properties);
    }

    /**
     * Obtains the information of all properties with a given amount of residents
     * @param amountOfResidents
     * @return List<PropertyDTO> with the information of all properties with a given amount of residents as a JSON
     */
    @GetMapping("/residents/{amountOfResidents}")
    public ResponseEntity<List<PropertyDTO>> findPropertiesByAmountOfResidents(@PathVariable Integer amountOfResidents) {
        List<PropertyDTO> properties = propertyService.findPropertiesByAmountOfResidents(amountOfResidents);
        return ResponseEntity.ok(properties);
    }
}
