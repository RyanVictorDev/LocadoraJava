package com.locadora.springboot.publishers.repositories;

import com.locadora.springboot.publishers.models.PublisherModel;
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
public interface PublisherRepository extends JpaRepository<PublisherModel, Integer> {
    UserDetails findByName(String name);
    PublisherModel findByNameAndIsDeletedFalse(String name);
    Page<PublisherModel> findAllByIsDeletedFalse(Pageable pageable);
    List<PublisherModel> findAllByIsDeletedFalse(Sort sort);
    PublisherModel findByEmail(String email);
    PublisherModel findByEmailAndIsDeletedFalse(String email);
    PublisherModel findBySite(String site);
    PublisherModel findBySiteAndIsDeletedFalse(String site);
    PublisherModel findByTelephone(String telephone);
    PublisherModel findByTelephoneAndIsDeletedFalse(String telephone);

    @Query("SELECT u FROM PublisherModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.site, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.telephone, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "AND u.isDeleted = false")
    List<PublisherModel> findAllByName(@Param("searchTerm") String searchTerm, Sort sort);

    @Query("SELECT u FROM PublisherModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.site, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.telephone, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "AND u.isDeleted = false")
    Page<PublisherModel> findAllByName(@Param("searchTerm") String searchTerm, Pageable pageable);
}
