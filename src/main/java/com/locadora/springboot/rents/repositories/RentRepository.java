package com.locadora.springboot.rents.repositories;

import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<RentModel, Integer> {
    boolean existsByBookIdAndStatus(int bookId, RentStatusEnum status);
    boolean existsByRenterIdAndStatus(int renterId, RentStatusEnum status);
    boolean existsByRenterIdAndBookIdAndStatus(int renterId, int bookId, RentStatusEnum status);
    List<RentModel> findAllByStatus(RentStatusEnum status);
    List<RentModel> findAllByRenterId(int renterId);
    List<RentModel> findAllByRenterIdAndStatus(int renterId, RentStatusEnum status);
    List<RentModel> findAllByBookId(int bookId);
    List<RentModel> findAllByBookIdAndStatus(int bookId, RentStatusEnum status);

    @Query("SELECT u FROM RentModel u " +
            "JOIN u.renter r " +
            "JOIN u.book b " +
            "WHERE LOWER(REPLACE(r.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:search, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(b.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:search, ' ', ''), '%'))")
    List<RentModel> findAllByRenterNameOrBookName(@Param("search") String search);
}
