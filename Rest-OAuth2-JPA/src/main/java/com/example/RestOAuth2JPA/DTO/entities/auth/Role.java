package com.example.RestOAuth2JPA.DTO.entities.auth;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(name = "ROLES_PRIVILEGES", 
               joinColumns = @JoinColumn(name = "role_id"),
               inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Collection<Privilege> privileges;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Collection<Privilege> getPrivileges() {
      return privileges;
    }

    public Role() {

    }

    public Role(String name) {
      this.name = name;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
      this.privileges = privileges;
    }
}
