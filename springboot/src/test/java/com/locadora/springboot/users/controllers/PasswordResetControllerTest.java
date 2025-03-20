package com.locadora.springboot.users.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.locadora.springboot.users.DTOs.TokenValidationRequest;
import com.locadora.springboot.users.models.EmailRequestModel;
import com.locadora.springboot.users.models.PasswordResetRequestModel;
import com.locadora.springboot.users.models.PasswordResetTokenModel;
import com.locadora.springboot.users.services.EmailServices;
import com.locadora.springboot.users.services.UserServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class PasswordResetControllerTest {

    @Mock
    private UserServices userServices;

    @Mock
    private EmailServices emailService;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Test
    public void testProcessForgotPasswordSuccess() {
        String email = "usuario@exemplo.com";
        String token = "valid-token";
        String userName = "UsuarioTeste";

        EmailRequestModel emailRequest = new EmailRequestModel();
        emailRequest.setEmail(email);

        when(userServices.createPasswordResetToken(email)).thenReturn(token);
//        lenient().when(userServices.getUserNameByEmail(email)).thenReturn(userName);

        ResponseEntity<String> response = passwordResetController.processForgotPassword(emailRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Instruções de redefinição de senha enviadas para " + email, response.getBody());
    }


    @Test
    public void testResetPasswordSuccess() {
        String token = "valid-token";
        String newPassword = "new-password";

        PasswordResetRequestModel passwordResetRequest = new PasswordResetRequestModel();
        passwordResetRequest.setToken(token);
        passwordResetRequest.setNewPassword(newPassword);

        when(userServices.resetPassword(token, newPassword)).thenReturn(true);

        ResponseEntity<String> response = passwordResetController.resetPassword(passwordResetRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha redefinida com sucesso.", response.getBody());
    }


    @Test
    public void testResetPasswordTokenInvalid() {
        String token = "invalid-token";
        String newPassword = "new-password";

        PasswordResetRequestModel passwordResetRequest = new PasswordResetRequestModel();
        passwordResetRequest.setToken(token);
        passwordResetRequest.setNewPassword(newPassword);


        when(userServices.resetPassword(token, newPassword)).thenReturn(false);

        ResponseEntity<String> response = passwordResetController.resetPassword(passwordResetRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Falha ao redefinir a senha. Token inválido ou expirado.", response.getBody());
    }


    @Test
    public void testValidateResetToken() {
        String token = "valid-token";

        TokenValidationRequest tokenRequest = new TokenValidationRequest();
        tokenRequest.setToken(token);

        when(userServices.validatePasswordResetToken(token)).thenReturn(true);

        ResponseEntity<String> response = passwordResetController.validateResetToken(tokenRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token válido.", response.getBody());
    }
}