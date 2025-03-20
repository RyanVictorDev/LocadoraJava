package com.locadora.springboot.users.repositories;

import com.locadora.springboot.users.models.PasswordResetTokenModel;
import com.locadora.springboot.users.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenModel, Long> {
    PasswordResetTokenModel findByToken(String token);
    PasswordResetTokenModel findByUser(UserModel user);
}