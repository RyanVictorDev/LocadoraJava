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

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 15, nullable = false)
    private String telephone;

    @Column(length = 180)
    private String site;

    @Column(nullable = false)
    private boolean isDeleted;

    public PublisherModel(String name, String email, String telephone, String site){
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.site = site;
        this.isDeleted = false;
    }
}
