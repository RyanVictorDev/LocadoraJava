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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String email;
    private int telephone;
    private String address;
    private int cpf;

    public RenterModel(String name, String email, int telephone, String address, int cpf){
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.cpf = cpf;
    }
}
