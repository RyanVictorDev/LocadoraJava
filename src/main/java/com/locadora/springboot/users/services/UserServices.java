package com.locadora.springboot.users.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UpdateUserRequestDTO;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.repositories.UserRepository;
import com.locadora.springboot.users.validations.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private UserValidation userValidation;

    public ResponseEntity<Void> create(@Valid CreateUserRequestDTO data) {

        userValidation.validateEmail(data);

        String encryptedPassword = passwordEncoder.encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<UserModel> findAll() {
        List<UserModel> users = userRepository.findAll();
        if (users.isEmpty()) throw new ModelNotFoundException();
        return users;
    }

    public Optional<UserModel> findById(int id) {
        return userRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateUserRequestDTO updateUserRequestDTO){
        Optional<UserModel> response = userRepository.findById(id);
        if(response.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var userModel = response.get();

        userValidation.validateUpdateEmail(updateUserRequestDTO);
        BeanUtils.copyProperties(updateUserRequestDTO, userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    public ResponseEntity<Object> delete(int id){
        Optional<UserModel> response = userRepository.findById(id);
        if(response.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.delete(response.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }
}
