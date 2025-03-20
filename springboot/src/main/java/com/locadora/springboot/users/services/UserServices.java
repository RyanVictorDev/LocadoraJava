package com.locadora.springboot.users.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UpdateUserRequestDTO;
import com.locadora.springboot.users.models.PasswordResetTokenModel;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.repositories.PasswordResetTokenRepository;
import com.locadora.springboot.users.repositories.UserRepository;
import com.locadora.springboot.users.validations.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    public ResponseEntity<Void> create(@Valid CreateUserRequestDTO data) {

        userValidation.create(data);

        String encryptedPassword = passwordEncoder.encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public Page<UserModel> findAll(String search, int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (Objects.isNull(search) || Objects.equals(search, "")) {
            Page<UserModel> users = userRepository.findAll(pageable);
            if (users.isEmpty()) throw new ModelNotFoundException();
            return users;
        } else {
            return userRepository.findAllByName(search, pageable);
        }
    }

    public Page<UserModel> findAllByRole(String search, int page, String role) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (Objects.equals(search, "")) {
            Page<UserModel> users = userRepository.findAllByRole(role, pageable);
            if (users.isEmpty()) throw new ModelNotFoundException();
            return users;
        } else {
            return userRepository.findAllByRoleAndSearch(role, search, pageable);
        }
    }

    public List<UserModel> findAllWithoutPagination(String search) {
        if (Objects.isNull(search) || Objects.equals(search, "")) {
            return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        } else {
            return userRepository.findAllByName(search, Sort.by(Sort.Direction.DESC, "id"));
        }
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

        userValidation.update(updateUserRequestDTO, id);

        userModel.setName(updateUserRequestDTO.name());
        userModel.setEmail(updateUserRequestDTO.email());
        userModel.setRole(updateUserRequestDTO.role());

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

    public String createPasswordResetToken(String email) {
        Optional<UserModel> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            return null;
        }

        UserModel user = userOptional.get();

        PasswordResetTokenModel existingToken = resetTokenRepository.findByUser(user);
        if (existingToken != null) {
            if (!existingToken.isExpired()) {
                return existingToken.getToken();
            }
            resetTokenRepository.delete(existingToken);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetTokenModel newToken = new PasswordResetTokenModel(token, user);
        resetTokenRepository.save(newToken);

        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetTokenModel resetToken = resetTokenRepository.findByToken(token);
        return resetToken != null && !resetToken.isExpired();
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordResetTokenModel resetToken = resetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            return false;
        }

        UserModel user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokenRepository.delete(resetToken);

        return true;
    }

    public String getUserNameByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getName();
        }
        return null;
    }
}
