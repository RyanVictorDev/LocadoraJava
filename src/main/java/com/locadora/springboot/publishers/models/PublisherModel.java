package com.locadora.springboot.publishers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_publishers")
public class PublisherModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String email;
    private int telephone;
    private String site;

    public PublisherModel(String name, String email, int telephone, String site){
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.site = site;
    }
}