package com.locadora.springboot.rents.models;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.renters.models.RenterModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_rents")
public class RentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private RenterModel renter;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookModel book;

    private LocalDate deadLine;

    private LocalDate devolutionDate;

    public RentModel(RenterModel renter, BookModel book, LocalDate deadLine){
        this.renter = renter;
        this.book = book;
        this.deadLine = deadLine;
        this.devolutionDate = LocalDate.now();
    }
}
