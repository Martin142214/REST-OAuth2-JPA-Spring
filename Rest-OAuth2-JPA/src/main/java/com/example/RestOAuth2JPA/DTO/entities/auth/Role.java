package com.example.RestOAuth2JPA.DTO.entities.auth;

import java.util.Collection;
import java.util.UUID;

import org.hibernate.mapping.List;

import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String name;

    /*@ManyToMany(mappedBy = "roles")
    private Collection<User> users;*/

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_privileges", 
               joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
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
