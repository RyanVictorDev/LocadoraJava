package com.locadora.springboot.users.repositories;

import com.locadora.springboot.users.models.UserModel;
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
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    UserDetails findByName(String name);
    UserModel findByEmail(String email);

    @Query("SELECT u FROM UserModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "OR LOWER(CASE WHEN u.role = 'USER' THEN 'LEITOR' ELSE 'EDITOR' END) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))")
    List<UserModel> findAllByName(@Param("searchTerm") String searchTerm, Sort sort);

    @Query("SELECT u FROM UserModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "OR LOWER(CASE WHEN u.role = 'USER' THEN 'LEITOR' ELSE 'EDITOR' END) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))")
    Page<UserModel> findAllByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM UserModel u WHERE LOWER(u.role) = LOWER(:role)")
    Page<UserModel> findAllByRole(@Param("role") String role, Pageable pageable);

    @Query("SELECT u FROM UserModel u WHERE " +
            "LOWER(u.role) = LOWER(:searchRole) AND (" +
            "LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.email, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')))")
    Page<UserModel> findAllByRoleAndSearch(@Param("searchRole") String searchRole, @Param("searchTerm") String searchTerm, Pageable pageable);
}
