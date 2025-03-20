package com.locadora.springboot.users.validations;

import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UpdateUserRequestDTO;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.models.UserRoleEnum;
import com.locadora.springboot.users.repositories.UserRepository;
import com.locadora.springboot.users.services.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {

    @Mock
    private UserServices userServices;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidation userValidation;

    private CreateUserRequestDTO createUserRequestDTO;
    private UpdateUserRequestDTO updateUserRequestDTO;
    private UserModel existingUser;

    @BeforeEach
    void setUp() {
        createUserRequestDTO = new CreateUserRequestDTO("John Doe", "john.doe@example.com", "password123", UserRoleEnum.USER);
        updateUserRequestDTO = new UpdateUserRequestDTO("John Doe", "john.doe@example.com", UserRoleEnum.USER);
        existingUser = new UserModel(1, "Existing User", "existing@example.com", "password123", UserRoleEnum.USER);
    }

    @Test
    void shouldThrowExceptionWhenNameIsInUse() {
        when(userRepository.findByName(createUserRequestDTO.name())).thenReturn(existingUser);

        assertThrows(CustomValidationException.class, () -> userValidation.create(createUserRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInUse() {
        when(userRepository.findByEmail(createUserRequestDTO.email())).thenReturn(existingUser);

        assertThrows(CustomValidationException.class, () -> userValidation.create(createUserRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        CreateUserRequestDTO nullNameDTO = new CreateUserRequestDTO(null, "john.doe@example.com", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(nullNameDTO), "O nome de usuário não pode estar vazio.");

        CreateUserRequestDTO emptyNameDTO = new CreateUserRequestDTO("", "john.doe@example.com", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(emptyNameDTO), "O nome de usuário não pode estar vazio.");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNullOrEmpty() {
        CreateUserRequestDTO nullNameDTO = new CreateUserRequestDTO("John", null, "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(nullNameDTO), "O email não pode estar vazio.");

        CreateUserRequestDTO emptyNameDTO = new CreateUserRequestDTO("John", "", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(emptyNameDTO), "O email não pode estar vazio.");
    }

    @Test
    void shouldCreateWhenValidData() {
        when(userRepository.findByName(createUserRequestDTO.name())).thenReturn(null);
        when(userRepository.findByEmail(createUserRequestDTO.email())).thenReturn(null);

        userValidation.create(createUserRequestDTO);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingName() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByName(updateUserRequestDTO.name())).thenReturn(new UserModel());

        assertThrows(CustomValidationException.class, () -> userValidation.update(updateUserRequestDTO, existingUser.getId()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(updateUserRequestDTO.email())).thenReturn(new UserModel());

        assertThrows(CustomValidationException.class, () -> userValidation.update(updateUserRequestDTO, existingUser.getId()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWhenNameIsNullOrEmpty() {
        UpdateUserRequestDTO nullNameDTO = new UpdateUserRequestDTO(null, "john.doe@example.com", UserRoleEnum.USER);
        assertThrows(NoSuchElementException.class, () -> userValidation.update(nullNameDTO, existingUser.getId()), "O nome de usuário não pode estar vazio.");

        UpdateUserRequestDTO emptyNameDTO = new UpdateUserRequestDTO("", "john.doe@example.com", UserRoleEnum.USER);
        assertThrows(NoSuchElementException.class, () -> userValidation.update(emptyNameDTO, existingUser.getId()), "O nome de usuário não pode estar vazio.");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWhenEmailIsNullOrEmpty() {
        UpdateUserRequestDTO nullNameDTO = new UpdateUserRequestDTO("John", null, UserRoleEnum.USER);
        assertThrows(NoSuchElementException.class, () -> userValidation.update(nullNameDTO, existingUser.getId()), "O email não pode estar vazio.");

        UpdateUserRequestDTO emptyNameDTO = new UpdateUserRequestDTO("John", "", UserRoleEnum.USER);
        assertThrows(NoSuchElementException.class, () -> userValidation.update(emptyNameDTO, existingUser.getId()), "O email não pode estar vazio.");
    }

    @Test
    void shouldUpdateWhenValidData() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByName(updateUserRequestDTO.name())).thenReturn(null);
        when(userRepository.findByEmail(updateUserRequestDTO.email())).thenReturn(null);

        userValidation.update(updateUserRequestDTO, existingUser.getId());
    }
}
