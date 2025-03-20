package com.locadora.springboot.books.repositories;

import com.locadora.springboot.books.models.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Integer> {
    BookModel findByName(String name);
    List<BookModel> findAllByNameAndIsDeletedFalse(String name);
    List<BookModel> findByPublisherId(int publisherId);
    List<BookModel> findAllByIsDeletedFalse(Sort sort);
    Page<BookModel> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT u FROM BookModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.author, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(CAST(u.launchDate AS string), ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(CAST(u.totalQuantity AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(CAST(u.totalInUse AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND u.isDeleted = false")
    Page<BookModel> findAllByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM BookModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.author, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(CAST(u.launchDate AS string), ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(CAST(u.totalQuantity AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(CAST(u.totalInUse AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND u.isDeleted = false")
    List<BookModel> findAllByName(@Param("searchTerm") String searchTerm, Sort sort);
}