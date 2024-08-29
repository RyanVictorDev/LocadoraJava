package com.locadora.springboot.rents.repositories;

import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<RentModel, Integer> {
    boolean existsByBookIdAndStatus(int bookId, RentStatusEnum status);
    boolean existsByRenterIdAndStatus(int renterId, RentStatusEnum status);
    boolean existsByRenterIdAndBookIdAndStatus(int renterId, int bookId, RentStatusEnum status);
}
