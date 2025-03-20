package com.locadora.springboot.users.services;

import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UpdateUserRequestDTO;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.models.UserRoleEnum;
import com.locadora.springboot.users.repositories.PasswordResetTokenRepository;
import com.locadora.springboot.users.repositories.UserRepository;
import com.locadora.springboot.users.validations.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserValidation userValidation;

    @Mock
    private PasswordResetTokenRepository resetTokenRepository;

    @InjectMocks
    private UserServices userServices;

    private CreateUserRequestDTO createUserRequestDTO;

    private UserModel user;

    @BeforeEach
    void setUp() {
        createUserRequestDTO = new CreateUserRequestDTO("John Doe", "john.doe@example.com", "password123", UserRoleEnum.USER);
        user = new UserModel("Ryan", "vryan8294@gmail.com", "123123", UserRoleEnum.ADMIN);
        user.setId(3);
    }

    @Test
    void shouldCreate() {
        String encryptedPassword = "encryptedPassword123";
        when(passwordEncoder.encode(createUserRequestDTO.password())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            user.setId(1);
            return user;
        });

        ResponseEntity<Void> response = userServices.create(createUserRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(userValidation, times(1)).create(createUserRequestDTO);

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserModel savedUser = userCaptor.getValue();
        assertEquals(createUserRequestDTO.name(), savedUser.getName());
        assertEquals(createUserRequestDTO.email(), savedUser.getEmail());
        assertEquals(encryptedPassword, savedUser.getPassword());
        assertEquals(createUserRequestDTO.role(), savedUser.getRole());
    }

    @Test
    void shouldFindById() {
        int userId = 3;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserModel> result = userServices.findById(userId);

        assertTrue(result.isPresent());
        assertEquals("Ryan", result.get().getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowModelNotFoundExceptionWhenFindAllWithEmptyResult() {
        int page = 0;
        String search = "";
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        when(userRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThrows(ModelNotFoundException.class, () -> userServices.findAll(search, page));
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldFindAllWithSearch() {
        String search = "John";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<UserModel> users = Arrays.asList(
                new UserModel("John Doe", "john.doe@example.com", "password1", UserRoleEnum.USER),
                user
        );
        Page<UserModel> userPage = new PageImpl<>(users);
        when(userRepository.findAllByName(search, pageable)).thenReturn(userPage);

        Page<UserModel> result = userServices.findAll(search, page);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAllByName(search, pageable);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        int userId = 1;
        UpdateUserRequestDTO updateUserRequestDTO = new UpdateUserRequestDTO("Jane Smith", "jane.smith@example.com", UserRoleEnum.ADMIN);
        UserModel existingUser = new UserModel("Jane Doe", "jane.doe@example.com", "password", UserRoleEnum.USER);
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserModel.class))).thenReturn(existingUser);

        ResponseEntity<Object> response = userServices.update(userId, updateUserRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingUser, response.getBody());

        verify(userValidation, times(1)).update(updateUserRequestDTO, userId);

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserModel savedUser = userCaptor.getValue();
        assertEquals(updateUserRequestDTO.name(), savedUser.getName());
        assertEquals(updateUserRequestDTO.email(), savedUser.getEmail());
        assertEquals(updateUserRequestDTO.role(), savedUser.getRole());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() {
        int userId = 1;
        UpdateUserRequestDTO updateUserRequestDTO = new UpdateUserRequestDTO("Jane Smith", "jane.smith@example.com", UserRoleEnum.ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userServices.update(userId, updateUserRequestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(UserModel.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        int userId = 1;
        UserModel user = new UserModel("John Doe", "john.doe@example.com", "password", UserRoleEnum.USER);
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<Object> response = userServices.delete(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUser() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userServices.delete(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).delete(any(UserModel.class));
    }

}
