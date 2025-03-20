package com.locadora.springboot.renters.repositories;

import com.locadora.springboot.renters.models.RenterModel;
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
public interface RenterRepository extends JpaRepository<RenterModel, Integer> {
    UserDetails findByName(String name);
    RenterModel findByEmail(String email);
    RenterModel findByEmailAndIsDeletedFalse(String email);
    RenterModel findByTelephoneAndIsDeletedFalse(String email);
    RenterModel findByCpf(String cpf);
    RenterModel findByCpfAndIsDeletedFalse(String cpf);
    Page<RenterModel> findAllByIsDeletedFalse(Pageable pageable);
    List<RenterModel> findAllByIsDeletedFalse(Sort sort);
    List<RenterModel> findAllByEmail(String email);

    @Query("SELECT u FROM RenterModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.cpf, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.address, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.telephone, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "AND u.isDeleted = false")
    List<RenterModel> findAllByName(@Param("searchTerm") String searchTerm, Sort sort);

    @Query("SELECT u FROM RenterModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.cpf, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.address, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.telephone, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "AND u.isDeleted = false")
    Page<RenterModel> findAllByName(@Param("searchTerm") String searchTerm, Pageable pageable);
}