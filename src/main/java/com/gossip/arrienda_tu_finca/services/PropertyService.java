package com.gossip.arrienda_tu_finca.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import java.util.Random;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private Random random;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // Obtener las propiedades de un municipio aleatorio
    public List<PropertyDTO> getPropertiesByRandomMunicipality() {
        List<String> municipalities = propertyRepository.findAllMunicipalities();
        // Seleccionar un municipio al azar
        String randomMunicipality = municipalities.get(random.nextInt(municipalities.size()));
        // Obtener las propiedades del municipio seleccionado
        return propertyRepository.findAllPropertyDTOByMunicipality(randomMunicipality);
    }

    // Obtener una propiedad por nombre
    public PropertyDTO getPropertyByName(String name) {
        PropertyDTO propertyDTO = propertyRepository.findPropertyDTOByName(name);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por municipio
    public PropertyDTO getPropertyByMunicipality(String municipality) {
        PropertyDTO propertyDTO = propertyRepository.findPropertyDTOByMunicipality(municipality);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por nombre
    public PropertyDTO getPropertyByPeopleNumber(Integer peopleNumber) {
        PropertyDTO propertyDTO = propertyRepository.findPropertyDTOByPeopleNumber(peopleNumber);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property not found");
        }
        return propertyDTO;
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

    // Convierte una entidad Property en PropertyDTO
    private PropertyDTO mapToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setDescription(property.getDescription());
        dto.setMunicipality(property.getMunicipality());
        dto.setTypeOfEntrance(property.getTypeOfEntrance());
        dto.setPeopleNumber(property.getPeopleNumber());
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

