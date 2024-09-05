package com.gossip.arrienda_tu_finca.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
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
    public List<PropertyListDTO> getPropertiesByRandomMunicipality() {
        List<String> municipalities = propertyRepository.findAllMunicipalities();
        // Seleccionar un municipio al azar
        String randomMunicipality = municipalities.get(random.nextInt(municipalities.size()));
        // Obtener las propiedades del municipio seleccionado
        return propertyRepository.findAllPropertyDTOByMunicipality(randomMunicipality);
    }

    // Obtener una propiedad por nombre
    public PropertyListDTO getPropertyByName(String name) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByName(name);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por municipio
    public PropertyListDTO getPropertyByMunicipality(String municipality) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByMunicipality(municipality);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por numero de personas
    public PropertyListDTO getPropertyByPeopleNumber(Integer peopleNumber) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByPeopleNumber(peopleNumber);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property not found");
        }
        return propertyDTO;
    }

    // Convierte una entidad Property en PropertyDTO
    private PropertyListDTO mapToDTO(Property property) {
        PropertyListDTO dto = new PropertyListDTO();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setMunicipality(property.getMunicipality());
        dto.setDepartment(property.getDepartment());
        dto.setPeopleNumber(property.getPeopleNumber());
        dto.setPropertyLink(property.getPropertyLink());
        dto.setPhotos(property.getPhotos());
        return dto;
    }
}

