package com.locadora.springboot.books.models;

import com.locadora.springboot.publishers.models.PublisherModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_books")
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private LocalDate launchDate;
    private int totalQuantity;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherModel publisher;

    public BookModel(String name, String author, LocalDate launchDate, int totalQuantity, PublisherModel publisher) {
        this.name = name;
        this.author = author;
        this.launchDate = launchDate;
        this.totalQuantity = totalQuantity;
        this.publisher = publisher;
    }
}
