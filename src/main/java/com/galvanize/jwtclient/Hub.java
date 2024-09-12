package com.galvanize.jwtclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hubs")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_name", nullable = false)
    private String name;

    @Column(name = "hub_picture")
    private String picture;

//    @OneToMany(mappedBy = "hub")
//    private List<Profile> profiles;


    public Hub() {}

    public Hub(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }


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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

//    public List<Profile> getProfiles() {
//        return profiles;
//    }
//
//    public void setProfiles(List<Profile> profiles) {
//        this.profiles = profiles;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hub that = (Hub) o;
        return Objects.equals(name, that.name) && Objects.equals(picture, that.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, picture);
    }
}
