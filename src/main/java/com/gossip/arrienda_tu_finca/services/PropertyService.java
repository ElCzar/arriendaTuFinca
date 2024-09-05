package com.gossip.arrienda_tu_finca.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
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
        this.random = new Random();  
    }

    public void setRandom(Random random) {
        this.random = random;
    }
    
    // Obtener las propiedades de un municipio aleatorio
    public List<PropertyListDTO> getPropertiesByRandomMunicipality() {
        List<String> municipalities = propertyRepository.findAllMunicipalities();
        String randomMunicipality = municipalities.get(random.nextInt(municipalities.size()));
        List<PropertyListDTO> properties = propertyRepository.findAllPropertyDTOByMunicipality(randomMunicipality);
        if (properties.isEmpty()) {
            throw new PropertyNotFoundException("Property by random municipality not found");
        }
        return properties;
    }

    // Obtener una propiedad por nombre
    public PropertyListDTO getPropertyByName(String name) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByName(name);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property by name not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por municipio
    public PropertyListDTO getPropertyByMunicipality(String municipality) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByMunicipality(municipality);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property by municipality not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por numero de personas
    public PropertyListDTO getPropertyByPeopleNumber(Integer peopleNumber) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByPeopleNumber(peopleNumber);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property by number of people not found");
        }
        return propertyDTO;
    }
}

