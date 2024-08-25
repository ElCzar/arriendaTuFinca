package com.gossip.arrienda_tu_finca.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private String email;
    private String name;
    private String surname;
    private String password;
    private String phone;
    private boolean isHost;
    private boolean isRenter;

    // OneToMany relationship with Property
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Property> properties;
}
