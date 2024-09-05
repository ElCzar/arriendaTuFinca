package com.gossip.arrienda_tu_finca.controllers;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
import com.gossip.arrienda_tu_finca.services.PropertyService;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.Collections;


@SpringBootTest
@AutoConfigureMockMvc
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PropertyService propertyService;
    
    // Test No. 1: getPropertiesByRandomMunicipality

    @Test // Test 1.1. Exito: Obtener una lista de propiedades en un municipio elegido aleatoriamente
    public void testGetPropertiesByRandomMunicipalitySuccess() throws Exception {
        List<PropertyListDTO> properties = Arrays.asList(
                new PropertyListDTO(1L, "Esmeralda", "Macheta", "Cundinamarca", 4, "www.katyperry.com", new byte[0], "katyperry@gmail.com")
        );
    
        Mockito.when(propertyService.getPropertiesByRandomMunicipality()).thenReturn(properties);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/random-municipality")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Esmeralda"))
                .andExpect(jsonPath("$[0].municipality").value("Macheta"));
    }

    @Test // Test 1.2. Fallo: Lanzar una excepción cuando no hay propiedades del municipio elegido aleatoriamente
    public void testGetPropertiesByRandomMunicipalityFailure() throws Exception {
 
        Mockito.when(propertyService.getPropertiesByRandomMunicipality())
               .thenThrow(new PropertyNotFoundException("Properties not found"));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/random-municipality")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())  
                .andExpect(jsonPath("$.message").value("Properties not found"));  
    }

     // Test No. 2: getPropertyByName

     @Test // Test 2.1. Exito: Obtener la propiedad correcta segun su nombre
     public void testGetPropertyByNameSuccess() throws Exception {
        PropertyListDTO property = new PropertyListDTO(1L, "Esmeralda", "Macheta", "Cundinamarca", 4, "www.katyperry.com", new byte[0], "katyperry@gmail.com");
    
        Mockito.when(propertyService.getPropertyByName("Esmeralda")).thenReturn(property);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/Esmeralda")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Esmeralda"))
                .andExpect(jsonPath("$.municipality").value("Macheta"));
    }

    @Test // Test 2.2. Fallo: Lanzar una excepción cuando no hay una propiedad con el nombre insertado
    public void testGetPropertyByNameFailure() throws Exception {
        Mockito.when(propertyService.getPropertyByName("Paraiso"))
                .thenThrow(new PropertyNotFoundException("Property not found"));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/Paraiso")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test No. 3: getPropertyByMunicipality

    @Test // Test 3.1. Exito: Obtener las propiedades de un municipio elegido
    public void testGetPropertyByMunicipalitySuccess() throws Exception {
        PropertyListDTO property = new PropertyListDTO(1L, "Esmeralda", "Macheta", "Cundinamarca", 4, "www.katyperry.com", new byte[0], "katyperry@gmail.com");
    
        Mockito.when(propertyService.getPropertyByMunicipality("Macheta")).thenReturn(property);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/Macheta")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Esmeralda"))
                .andExpect(jsonPath("$.municipality").value("Macheta"));
    }
 
    @Test // Test 3.2. Fallo: Lanzar una excepción cuando no hay propiedades del municipio insertado
    public void testGetPropertyByMunicipalityFailure() throws Exception {
        Mockito.when(propertyService.getPropertyByMunicipality("Cajica"))
                .thenThrow(new PropertyNotFoundException("Property not found"));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/Cajica")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test No. 4: getPropertyByPeopleNumber

    @Test // Test 4.1. Exito: Obtener las propiedades que coincidan con un numero de personas
    public void testGetPropertyByPeopleNumberSuccess() throws Exception {
        PropertyListDTO property = new PropertyListDTO(1L, "Esmeralda", "Macheta", "Cundinamarca", 4, "www.katyperry.com", new byte[0], "katyperry@gmail.com");
    
        Mockito.when(propertyService.getPropertyByPeopleNumber(4)).thenReturn(property);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Esmeralda"))
                .andExpect(jsonPath("$.peopleNumber").value(4));
    }

    @Test // Test 4.2. Fallo: Lanzar una excepción cuando no hay propiedades que coincidan con el numero de personas insertado
    public void testGetPropertyByPeopleNumberFailure() throws Exception {
        Mockito.when(propertyService.getPropertyByPeopleNumber(100))
                .thenThrow(new PropertyNotFoundException("Property not found"));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/property/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }







     







}
