package com.locadora.springboot.books.repositories;

import com.locadora.springboot.books.models.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Integer> {
    UserDetails findByName(String name);
    List<BookModel> findByPublisherId(int publisherId);
}