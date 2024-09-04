package com.gossip.arrienda_tu_finca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gossip.arrienda_tu_finca.dto.PropertyCreateDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;


@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public PropertyDTO createProperty(PropertyCreateDTO propertyCreateDTO) {
        Property property = new Property();
        property.setName(propertyCreateDTO.getName());
        property.setDescription(propertyCreateDTO.getDescription());
        property.setMunicipality(propertyCreateDTO.getMunicipality());
        property.setTypeOfEntrance(propertyCreateDTO.getTypeOfEntrance());
        property.setAddress(propertyCreateDTO.getAddress());
        property.setPricePerNight(propertyCreateDTO.getPricePerNight());
        property.setAmountOfRooms(propertyCreateDTO.getAmountOfRooms());
        property.setAmountOfBathrooms(propertyCreateDTO.getAmountOfBathrooms());
        property.setPetFriendly(propertyCreateDTO.isPetFriendly());
        property.setHasPool(propertyCreateDTO.isHasPool());
        property.setHasGril(propertyCreateDTO.isHasGril());
        property.setAvailable(true); // Propiedad disponible por defecto
       
        Property savedProperty = propertyRepository.save(property);
        return mapToDTO(savedProperty); // Convertimos la propiedad guardada a DTO
    }

    public PropertyDTO updateProperty(Long id, PropertyCreateDTO propertyCreateDTO) {
        Property property = propertyRepository.findById(id).orElseThrow();
        property.setName(propertyCreateDTO.getName());
        property.setDescription(propertyCreateDTO.getDescription());
        property.setMunicipality(propertyCreateDTO.getMunicipality());
        property.setTypeOfEntrance(propertyCreateDTO.getTypeOfEntrance());
        property.setAddress(propertyCreateDTO.getAddress());
        property.setPricePerNight(propertyCreateDTO.getPricePerNight());
        property.setAmountOfRooms(propertyCreateDTO.getAmountOfRooms());
        property.setAmountOfBathrooms(propertyCreateDTO.getAmountOfBathrooms());
        property.setPetFriendly(propertyCreateDTO.isPetFriendly());
        property.setHasPool(propertyCreateDTO.isHasPool());
        property.setHasGril(propertyCreateDTO.isHasGril());
        property.setAvailable(true); // Propiedad disponible por defecto
       
        Property savedProperty = propertyRepository.save(property);
        return mapToDTO(savedProperty); // Convertimos la propiedad guardada a DTO
    }
   

    private PropertyDTO mapToDTO(Property property) {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(property.getId());
        propertyDTO.setName(property.getName());
        propertyDTO.setDescription(property.getDescription());
        propertyDTO.setMunicipality(property.getMunicipality());
        propertyDTO.setTypeOfEntrance(property.getTypeOfEntrance());
        propertyDTO.setAddress(property.getAddress());
        propertyDTO.setPricePerNight(property.getPricePerNight());
        propertyDTO.setAmountOfRooms(property.getAmountOfRooms());
        propertyDTO.setAmountOfBathrooms(property.getAmountOfBathrooms());
        propertyDTO.setPetFriendly(property.isPetFriendly());
        propertyDTO.setHasPool(property.isHasPool());
        propertyDTO.setHasGril(property.isHasGril());
        propertyDTO.setAvailable(property.isAvailable());
        return propertyDTO;
    }

    
}