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
    private String name;
    private String email;
    private String telephone;
    private String address;
    private String cpf;

    public RenterModel(String name, String email, String telephone, String address, String cpf){
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.cpf = cpf;
    }
}
