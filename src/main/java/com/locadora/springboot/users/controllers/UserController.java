package com.locadora.springboot.users.controllers;

import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UserResponseDTO;
import com.locadora.springboot.users.mappers.UserMapper;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.repositories.UserRepository;
import com.locadora.springboot.users.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserServices userServices;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateUserRequestDTO data) {
        return userServices.create(data);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserResponseList(userServices.findAll()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserResponse(userServices.findById(id).get()));
    }

//
//    @PutMapping("/users/{id}")
//    public ResponseEntity<Object> updateUser(@PathVariable(value="id") int id, @RequestBody @Valid CreateUserRequestDTO createUserRequestDTO){
//        Optional<UserModel> userA = userRepository.findById(id);
//        if(userA.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//        var userModel = userA.get();
//        BeanUtils.copyProperties(createUserRequestDTO, userModel);
//        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
//    }
//
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Object> deleteUser(@PathVariable(value="id") int id){
//        Optional<UserModel> userA = userRepository.findById(id);
//        if(userA.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//        userRepository.delete(userA.get());
//        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
//    }
}
