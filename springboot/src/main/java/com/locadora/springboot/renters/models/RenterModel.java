package com.locadora.springboot.renters.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_renters")
public class RenterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 15, nullable = false)
    private String telephone;

    @Column(length = 80, nullable = false)
    private String address;

    @Column(length = 14, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private boolean isDeleted;

    public RenterModel(String name, String email, String telephone, String address, String cpf){
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.cpf = cpf;
        this.isDeleted = false;
    }
}
