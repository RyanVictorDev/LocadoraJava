package com.locadora.springboot.users.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserModel> findAll() {
        List<UserModel> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ModelNotFoundException();
        }
        return users;
    }

    public Optional<UserModel> findById(int id) {
        return userRepository.findById(id);
    }

    public ResponseEntity<Void> create(@Valid CreateUserRequestDTO data) {
        if (userRepository.findByName(data.name()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    public ResponseEntity<Void> create(@Valid CreateUserRequestDTO data) {
//        if (userRepository.findByName(data.name()) != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        String encryptedPassword = passwordEncoder.encode(data.password());
//        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword, data.role());
//        userRepository.save(newUser);
//    }
}
