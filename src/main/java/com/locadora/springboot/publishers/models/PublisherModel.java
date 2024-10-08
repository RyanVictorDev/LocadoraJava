package com.locadora.springboot.publishers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_publishers")
public class PublisherModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String telephone;
    private String site;
    private boolean isDeleted;

    public PublisherModel(String name, String email, String telephone, String site){
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.site = site;
        this.isDeleted = false;
    }
}
