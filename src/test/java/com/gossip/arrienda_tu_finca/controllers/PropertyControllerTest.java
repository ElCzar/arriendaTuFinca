package com.gossip.arrienda_tu_finca.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.services.PropertyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Random;

class PropertyControllerTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        propertyService = new PropertyService(propertyRepository);
    }

     // Test No. 1: getPropertiesByRandomMunicipality

    @Test  // Test No. 1: Exito: Obtener las propiedades de un municipio aleatorio
    void testGetPropertiesByRandomMunicipality() {
        List<String> municipalities = List.of("Medellín", "Bogotá", "Cali");
        List<PropertyListDTO> propertyDTOs = List.of(new PropertyListDTO(1L, "Finca A", "Medellín", "Antioquia", 10, "link1", null, "owner@example.com"));

        when(propertyRepository.findAllMunicipalities()).thenReturn(municipalities);
        when(propertyRepository.findAllPropertyDTOByMunicipality("Medellín")).thenReturn(propertyDTOs);

        List<PropertyListDTO> result = propertyService.getPropertiesByRandomMunicipality();

        assertFalse(result.isEmpty());
        assertEquals("Medellín", result.get(0).getMunicipality());
    }

    // Test No. 2: getPropertyByName

    @Test // Test 2.1. Exito: Obtener la propiedad correcta segun su nombre
    void testGetPropertyByame() {
        PropertyListDTO propertyDTO = new PropertyListDTO(1L, "Finca A", "Medellín", "Antioquia", 10, "link1", null, "owner@example.com");

        when(propertyRepository.findPropertyDTOByName("Finca A")).thenReturn(propertyDTO);

        PropertyListDTO result = propertyService.getPropertyByName("Finca A");

        assertNotNull(result);
        assertEquals("Finca A", result.getName());
    }

    @Test // Test 2.2. Fallo: Lanzar una excepción cuando no hay una propiedad con el nombre insertado
    void testGetPropertyByName_NotFound() {
        when(propertyRepository.findPropertyDTOByName("Finca B")).thenReturn(null);

        assertThrows(PropertyNotFoundException.class, () -> propertyService.getPropertyByName("Finca B"));
    }

    // Test No. 3: getPropertyByMunicipality

    @Test // Test 3.1. Exito: Obtener las propiedades de un municipio elegido
    void testGetPropertyByMunicipality() {
        PropertyListDTO propertyDTO = new PropertyListDTO(1L, "Finca A", "Medellín", "Antioquia", 10, "link1", null, "owner@example.com");

        when(propertyRepository.findPropertyDTOByMunicipality("Medellín")).thenReturn(propertyDTO);

        PropertyListDTO result = propertyService.getPropertyByMunicipality("Medellín");

        assertNotNull(result);
        assertEquals("Medellín", result.getMunicipality());
    }

    @Test // Test 3.2. Fallo: Lanzar una excepción cuando no hay propiedades del municipio insertado
    void testGetPropertyByMunicipality_NotFound() {
        when(propertyRepository.findPropertyDTOByMunicipality("Bogotá")).thenReturn(null);

        assertThrows(PropertyNotFoundException.class, () -> propertyService.getPropertyByMunicipality("Bogotá"));
    }

    // Test No. 4: getPropertyByPeopleNumber

    @Test // Test 4.1. Exito: Obtener las propiedades que coincidan con un numero de personas
    void testGetPropertyByPeopleNumber() {
        PropertyListDTO propertyDTO = new PropertyListDTO(1L, "Finca A", "Medellín", "Antioquia", 10, "link1", null, "owner@example.com");

        when(propertyRepository.findPropertyDTOByPeopleNumber(10)).thenReturn(propertyDTO);

        PropertyListDTO result = propertyService.getPropertyByPeopleNumber(10);

        assertNotNull(result);
        assertEquals(10, result.getPeopleNumber());
    }

    @Test // Test 4.2. Fallo: Lanzar una excepción cuando no hay propiedades que coincidan con el numero de personas insertado
    void testGetPropertyByPeopleNumber_NotFound() {
        when(propertyRepository.findPropertyDTOByPeopleNumber(5)).thenReturn(null);

        assertThrows(PropertyNotFoundException.class, () -> propertyService.getPropertyByPeopleNumber(5));
    }
}