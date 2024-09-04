package com.gossip.arrienda_tu_finca.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.PropertyCreateDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyUpdateDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // Crear una propiedad
    public PropertyDTO createProperty(PropertyCreateDTO propertyCreateDTO) {
        Property property = new Property();
        mapCreateDTOToEntity(propertyCreateDTO, property);
        property.setAvailable(true); // Propiedad disponible por defecto

        Property savedProperty = propertyRepository.save(property);
        return mapToDTO(savedProperty);
    }

    // Obtener una propiedad por ID
    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        return mapToDTO(property);
    }

    // Obtener todas las propiedades
    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Actualizar una propiedad
    public PropertyDTO updateProperty(Long id, PropertyUpdateDTO propertyUpdateDTO) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        mapUpdateDTOToEntity(propertyUpdateDTO, property);
        Property updatedProperty = propertyRepository.save(property);
        return mapToDTO(updatedProperty);
    }

    // Desactivar una propiedad
    public void deactivateProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        property.setAvailable(false); // Cambiar a no disponible
        propertyRepository.save(property);
    }

    // Subir una foto
    public void uploadPhoto(Long id, MultipartFile photo) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        try {
            byte[] photoBytes = photo.getBytes();
            // Aquí puedes guardar los bytes de la foto en la entidad o manejarlo de otra forma
            // property.setPhoto(photoBytes); // Si tienes un campo photo en Property
            propertyRepository.save(property);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la foto", e);
        }
    }

    // Mapea PropertyCreateDTO a una entidad Property
    private void mapCreateDTOToEntity(PropertyCreateDTO dto, Property property) {
        property.setName(dto.getName());
        property.setDescription(dto.getDescription());
        property.setMunicipality(dto.getMunicipality());
        property.setTypeOfEntrance(dto.getTypeOfEntrance());
        property.setAddress(dto.getAddress());
        property.setPricePerNight(dto.getPricePerNight());
        property.setAmountOfRooms(dto.getAmountOfRooms());
        property.setAmountOfBathrooms(dto.getAmountOfBathrooms());
        property.setPetFriendly(dto.isPetFriendly());
        property.setHasPool(dto.isHasPool());
        property.setHasGril(dto.isHasGril());
    }

    // Mapea PropertyUpdateDTO a una entidad Property
    private void mapUpdateDTOToEntity(PropertyUpdateDTO dto, Property property) {
        property.setName(dto.getName());
        property.setDescription(dto.getDescription());
        property.setMunicipality(dto.getMunicipality());
        property.setTypeOfEntrance(dto.getTypeOfEntrance());
        property.setAddress(dto.getAddress());
        property.setPricePerNight(dto.getPricePerNight());
        property.setAmountOfRooms(dto.getAmountOfRooms());
        property.setAmountOfBathrooms(dto.getAmountOfBathrooms());
        property.setPetFriendly(dto.isPetFriendly());
        property.setHasPool(dto.isHasPool());
        property.setHasGril(dto.isHasGril());
    }

    // Convierte una entidad Property en PropertyDTO
    private PropertyDTO mapToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setDescription(property.getDescription());
        dto.setMunicipality(property.getMunicipality());
        dto.setTypeOfEntrance(property.getTypeOfEntrance());
        dto.setAddress(property.getAddress());
        dto.setPricePerNight(property.getPricePerNight());
        dto.setAmountOfRooms(property.getAmountOfRooms());
        dto.setAmountOfBathrooms(property.getAmountOfBathrooms());
        dto.setPetFriendly(property.isPetFriendly());
        dto.setHasPool(property.isHasPool());
        dto.setHasGril(property.isHasGril());
        dto.setAvailable(property.isAvailable());
        return dto;
    }
}
