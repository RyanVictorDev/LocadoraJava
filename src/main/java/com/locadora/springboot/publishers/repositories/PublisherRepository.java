package com.locadora.springboot.publishers.repositories;

import com.locadora.springboot.publishers.models.PublisherModel;
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
    List<PublisherModel> findAllByIsDeletedFalse(Sort id);
    PublisherModel findByEmail(String email);
    PublisherModel findBySite(String site);
    PublisherModel findByTelephone(String telephone);

    @Query("SELECT u FROM PublisherModel u WHERE LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%'))")
    List<PublisherModel> findAllByName(@Param("name") String name);
}
