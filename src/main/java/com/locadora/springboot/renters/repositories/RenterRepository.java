package com.locadora.springboot.renters.repositories;

import com.locadora.springboot.renters.models.RenterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface RenterRepository extends JpaRepository<RenterModel, Integer> {
    UserDetails findByName(String name);
    RenterModel findByEmail(String email);
    RenterModel findByCpf(String cpf);
}