package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ArriendaTuFincaApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-test.properties"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TestPropertyController {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        propertyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @Test
    @DirtiesContext
    @Transactional
    @Description("Test the property creation where all the fields are valid")
    void givenCorrectProperty_whenCreateProperty_thenPropertyCreated() throws Exception {
        // Arrange
        String request = 
        """
        {
            "name": "Finca La Esperanza",
            "description": "Hermosa finca en el campo",
            "municipality": "Medellin",
            "department": "Antioquia",
            "typeOfEntrance": "Carretera",
            "address": "Km 10 via a El Retiro",
            "link": "www.fincaesperanza.com",
            "pricePerNight": 150.0,
            "amountOfRooms": 3,
            "amountOfBathrooms": 2,
            "amountOfResidents": 6,
            "isPetFriendly": true,
            "hasPool": true,
            "hasGril": true,
            "ownerEmail": "johnDoe@gmail.com"
        }
        """;
    
        // Create user with same email
        User user = new User();
        user.setEmail("johnDoe@gmail.com");
        user.setPassword("password");
        user.setId(1L);
        userRepository.save(user);

        // Act
        mvc.perform(MockMvcRequestBuilders.post("/property")
            .contentType("application/json")
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isOk());
    
        // Assert
        List<Property> properties = propertyRepository.findAll();
        assertEquals(1, properties.size());
        Property property = properties.get(0);
        assertEquals("Finca La Esperanza", property.getName());
        assertEquals("Hermosa finca en el campo", property.getDescription());
    }
    


//Test 2: Crear una propiedad con datos inválidos
 @Test
@DirtiesContext
@Transactional
@Description("Test to create a property with invalid data")
void givenInvalidData_whenCreateProperty_thenBadRequest() throws Exception {
    // Crea un JSON inválido (falta el campo 'name' por ejemplo)
    String invalidRequest = """
        {
            "description": "Hermosa finca en el campo",
            "municipality": "Medellin",
            "department": "Antioquia",
            "typeOfEntrance": "Carretera",
            "address": "Km 10 via a El Retiro",
            "link": "www.fincaesperanza.com",
            "pricePerNight": 150.0,
            "amountOfRooms": 3,
            "amountOfBathrooms": 2,
            "amountOfResidents": 5,
            "isPetFriendly": true,
            "hasPool": true,
            "hasGril": true,
            "ownerEmail": "owner@example.com"
        }
    """;

    // Act & Assert
    mvc.perform(MockMvcRequestBuilders.post("/property")
            .contentType("application/json")
            .content(invalidRequest))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());  // Esperamos 400 Bad Request
}

    // 3. Caso de éxito: Obtener propiedad por ID
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get a property by ID")
    void givenValidPropertyId_whenGetPropertyById_thenReturnProperty() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);
    
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/" + property.getId())
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Finca Bella"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Hermosa finca"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.municipality").value("Bogota"));
    }

    // 4. Caso de error: Obtener propiedad con ID inválido
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get a property by invalid ID")
    void givenInvalidPropertyId_whenGetPropertyById_thenNotFound() throws Exception {
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/999")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())  // Debe devolver 404 Not Found
                .andExpect(MockMvcResultMatchers.content().string("Property with ID 999 not found for fetching"));  // Verifica el mensaje
    }
    

    // 5. Caso de éxito: Actualizar una propiedad
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to update a property")
    void givenValidUpdateRequest_whenUpdateProperty_thenPropertyUpdated() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        String updateRequest = 
        """
        {
            "name": "Finca Actualizada",
            "description": "Finca con piscina",
            "municipality": "Bogota",
            "department": "Bogotá D.C.",
            "link": "www.fincaactualizada.com",
            "pricePerNight": 250.0,
            "amountOfRooms": 5,
            "amountOfBathrooms": 3,
            "amountOfResidents": 4,
            "isPetFriendly": true,
            "hasPool": true,
            "hasGril": true
        }
        """;

        // Act
        mvc.perform(MockMvcRequestBuilders.put("/property/" + property.getId())
            .contentType("application/json")
            .content(updateRequest))
            .andExpect(MockMvcResultMatchers.status().isOk());

        // Assert
        Property updatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertEquals("Finca Actualizada", updatedProperty.getName());
    }

    //6. Caso de error: Actualizar una propiedad con ID inválido
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to update a property with an invalid ID and expect a 404 Not Found")
    void givenInvalidId_whenUpdateProperty_thenNotFound() throws Exception {
        // Arrange
        String updateRequest = """
        {
            "name": "Finca Actualizada",
            "description": "Finca con piscina",
            "municipality": "Bogota",
            "department": "Bogotá D.C.",
            "link": "www.fincaactualizada.com",
            "pricePerNight": 250.0,
            "amountOfRooms": 5,
            "amountOfBathrooms": 3,
            "amountOfResidents": 4,
            "isPetFriendly": true,
            "hasPool": true,
            "hasGril": true
        }
        """;
    
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.put("/property/999")
            .contentType("application/json")
            .content(updateRequest))
            .andExpect(MockMvcResultMatchers.status().isNotFound()) // Espera un 404 Not Found
            .andExpect(MockMvcResultMatchers.content().string("To update property with ID 999 not found")); // Verifica el mensaje
    }
    
    

    // 7. Caso de éxito: Desactivar una propiedad
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to deactivate a property")
    void givenValidProperty_whenDeactivateProperty_thenPropertyDeactivated() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        property.setAvailable(true); // Initially available
        propertyRepository.save(property);

        // Act
        mvc.perform(MockMvcRequestBuilders.delete("/property/" + property.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Assert
        Property deactivatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertFalse(deactivatedProperty.isAvailable());
    }

    // 8. Caso de error: Intentar desactivar propiedad con ID inválido
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to deactivate a property with invalid ID")
    void givenInvalidPropertyId_whenDeactivateProperty_thenNotFound() throws Exception {
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.delete("/property/999")  // Usa un ID inexistente como 999
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());  // Debe devolver 404 Not Found
    }
    
    

    // 9. Caso de éxito: Subir una foto a una propiedad
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to upload a photo to a property")
    void givenValidProperty_whenUploadPhoto_thenPhotoUploaded() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        propertyRepository.save(property);

        MockMultipartFile photoFile = new MockMultipartFile("photo", "finca.jpg", "image/jpeg", "fake-image-content".getBytes());

        // Act
        mvc.perform(MockMvcRequestBuilders.multipart("/property/" + property.getId() + "/upload-photo")
            .file(photoFile))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // 10. Caso de error: Subir una foto a una propiedad con ID inexistente
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to upload a photo to a property with non-existent ID")
    void givenInvalidId_whenUploadPhoto_thenNotFound() throws Exception {
        MockMultipartFile photoFile = new MockMultipartFile("photo", "finca.jpg", "image/jpeg", "fake-image-content".getBytes());

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.multipart("/property/999/upload-photo")  // ID no existente
            .file(photoFile))
            .andExpect(MockMvcResultMatchers.status().isNotFound()); 
    }
    

    // 11. Caso de éxito: Obtener todas las propiedades
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get all properties")
    void testGetAllProperties() throws Exception {
        // Arrange
        Property property1 = new Property();
        property1.setName("Finca Bella");
        property1.setDescription("Hermosa finca");
        property1.setMunicipality("Bogota");
        propertyRepository.save(property1);

        Property property2 = new Property();
        property2.setName("Finca La Esperanza");
        property2.setDescription("Finca en el campo");
        property2.setMunicipality("Medellin");
        propertyRepository.save(property2);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Finca Bella"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Finca La Esperanza"));
    }

    // Arrendatario

    // 12. Caso de éxito: Obtener todas las propiedades de un municipio aleatorio
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get all properties from a random municipality")
    void givenPropertiesInRandomMunicipality_whenGetPropertiesByRandomMunicipality_thenReturnProperties() throws Exception {
        // Arrange
        Property property1 = new Property();
        property1.setName("Finca Bella");
        property1.setDescription("Hermosa finca");
        property1.setMunicipality("Bogota");
        propertyRepository.save(property1);

        Property property2 = new Property();
        property2.setName("Finca La Esperanza");
        property2.setDescription("Finca en el campo");
        property2.setMunicipality("Bogota");
        propertyRepository.save(property2);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/random-municipality")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].municipality").value("Bogota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].municipality").value("Bogota"));
    }

    // 13. Caso de éxito: Obtener todas las propiedades que coincidan con un nombre en especifico
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get properties by name")
    void givenValidPropertyName_whenGetPropertiesByName_thenReturnProperties() throws Exception {
        // Arrange
        Property property1 = new Property();
        property1.setName("Finca Bella");
        property1.setDescription("Hermosa finca");
        property1.setMunicipality("Bogota");
        propertyRepository.save(property1);

        Property property2 = new Property();
        property2.setName("Finca Bella");
        property2.setDescription("Finca en el campo");
        property2.setMunicipality("Bogota");
        propertyRepository.save(property2);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/name/Finca Bella")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Finca Bella"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Finca Bella"));
    }
    
    // 14. Caso de error: Obtener todas las propiedades que coincidan con un nombre invalido
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get properties by invalid name")
    void givenInvalidPropertyName_whenGetPropertiesByName_thenNotFound() throws Exception {
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/name/NombreInvalido")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())  // Debe devolver 404 Not Found
                .andExpect(MockMvcResultMatchers.content().string("Propiedades con el nombre NombreInvalido no fueron encontradas"));  // Verifica el mensaje
    }
    

    // 15. Caso de éxito: Obtener todas las propiedades que hagan parte de un municipio en especifico
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get properties by municipality")
    void givenValidMunicipality_whenGetPropertiesByMunicipality_thenReturnProperties() throws Exception {
        // Arrange
        Property property1 = new Property();
        property1.setName("Finca Bella");
        property1.setDescription("Hermosa finca");
        property1.setMunicipality("Bogota");
        propertyRepository.save(property1);
    
        Property property2 = new Property();
        property2.setName("Finca La Esperanza");
        property2.setDescription("Finca en el campo");
        property2.setMunicipality("Bogota");
        propertyRepository.save(property2);
    
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/municipality/Bogota")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].municipality").value("Bogota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].municipality").value("Bogota"));
    }
         

    // 16. Caso de error: Obtener todas las propiedades que hagan parte de un municipio invalido
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get properties by invalid municipality")
    void givenInvalidMunicipality_whenGetPropertiesByMunicipality_thenNotFound() throws Exception {
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/municipality/Barranquilla")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())  // Debe devolver 404 Not Found
                .andExpect(MockMvcResultMatchers.content().string("Propiedades del municipio Barranquilla no fueron encontradas"));  // Verifica el mensaje
    }

    // 17. Caso de éxito: Obtener todas las propiedades que tengan una cantidad de residentes en especifico
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get properties by amount of residents")
    void givenValidAmountOfResidents_whenGetPropertiesByAmountOfResidents_thenReturnProperties() throws Exception {
        // Arrange
        Property property1 = new Property();
        property1.setName("Finca Bella");
        property1.setDescription("Hermosa finca");
        property1.setMunicipality("Bogota");
        property1.setAmountOfResidents(4);
        propertyRepository.save(property1);
    
        Property property2 = new Property();
        property2.setName("Finca La Esperanza");
        property2.setDescription("Finca en el campo");
        property2.setMunicipality("Bogota");
        property2.setAmountOfResidents(4);
        propertyRepository.save(property2);
    
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/residents/4")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amountOfResidents").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amountOfResidents").value(4));
    }

    // 18. Caso de error: Obtener todas las propiedades que tengan una cantidad de residentes invalida
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get properties by invalid amount of residents")
    void givenInvalidAmountOfResidents_whenGetPropertiesByAmountOfResidents_thenNotFound() throws Exception {
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/residents/999")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())  // Debe devolver 404 Not Found
                .andExpect(MockMvcResultMatchers.content().string("Propiedades con cantidad de residentes 999 no fueron encontradas"));  // Verifica el mensaje
    }
}

    