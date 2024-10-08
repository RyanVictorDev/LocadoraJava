package com.locadora.springboot.rents.models;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.renters.models.RenterModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private LocalDate devolutionDate;

    private LocalDate deadLine;

    private LocalDate rentDate;

    @Enumerated(EnumType.STRING)
    private RentStatusEnum status;

    public RentModel(RenterModel renter, BookModel book, LocalDate deadLine){
        this.renter = renter;
        this.book = book;
        this.deadLine = deadLine;
        this.rentDate = LocalDate.now();
        this.status = null;
    }
}
