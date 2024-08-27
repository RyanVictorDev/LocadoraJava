package com.locadora.springboot.rents.repositories;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.rents.models.RentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<RentModel, Integer> {

}
