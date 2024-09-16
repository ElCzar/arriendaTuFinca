package com.gossip.arrienda_tu_finca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.entities.Property;

@Repository // Añadir esta anotación para asegurarte de que Spring la detecte como un repositorio
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Encuentra todas las propiedades que pertenecen a un usuario específico (por su email)
    @Query("SELECT p FROM Property p WHERE p.owner.email = :email")
    List<Property> findAllByOwnerEmail(@Param("email") String ownerEmail);

    // Consulta personalizada para obtener una propiedad como DTO usando su ID
    @Query("SELECT new com.gossip.arrienda_tu_finca.dto.PropertyDTO(p.id, p.name, p.description, p.municipality, p.department, p.typeOfEntrance, p.address, p.link, p.isAvailable, p.pricePerNight, p.amountOfRooms, p.amountOfBathrooms, p.amountOfResidents, p.isPetFriendly, p.hasPool, p.hasGril, p.owner.email) FROM Property p WHERE p.id = :propertyId")
    PropertyDTO findPropertyDTOById(@Param("propertyId") Long propertyId);

    // Encuentra todas las propiedades disponibles (activas)
    @Query("SELECT p FROM Property p WHERE p.isAvailable = true")
    List<Property> findAllAvailableProperties();

    // Verifica si una propiedad existe por su ID y si está disponible
    @Query("SELECT COUNT(p) > 0 FROM Property p WHERE p.id = :propertyId AND p.isAvailable = true")
    boolean existsByIdAndIsAvailable(@Param("propertyId") Long propertyId);

    // Devuelve el precio por noche de una propiedad específica
    @Query("SELECT p.pricePerNight FROM Property p WHERE p.id = :propertyId")
    Double findPricePerNightById(@Param("propertyId") Long propertyId);

    // Modifica el estado de disponibilidad de una propiedad por su ID
    @Modifying
    @Transactional // Importante para los métodos que modifican la base de datos
    @Query("UPDATE Property p SET p.isAvailable = false WHERE p.id = :propertyId")
    void deactivatePropertyById(@Param("propertyId") Long propertyId);

    // Arrendatario

    // Encuentra todas las propiedades que pertenecen a un municipio aleatorio
    @Query("SELECT p.municipality FROM Property p ORDER BY RAND()")
    String findRandomMunicipality();

    // Encuentra todas las propiedades con un nombre especifico
    @Query("SELECT p FROM Property p WHERE p.name = :name")
    List<Property> findPropertiesByName(@Param("name") String name);
    
    // Encuentra todas las propiedades de un municipio especifico
    @Query("SELECT p FROM Property p WHERE p.municipality = :municipality")
    List<Property> findPropertiesByMunicipality(@Param("municipality") String municipality);

    // Encuentra todas las propiedades con una cantidad de residentes especifica
    @Query("SELECT p FROM Property p WHERE p.amountOfResidents = :amountOfResidents")
    List<Property> findPropertiesByAmountOfResidents(@Param("amountOfResidents") Integer amountOfResidents);

}

