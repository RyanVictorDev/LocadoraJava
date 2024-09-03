package com.locadora.springboot.users.controllers;

import com.locadora.springboot.users.DTOs.TokenValidationRequest;
import com.locadora.springboot.users.models.EmailRequestModel;
import com.locadora.springboot.users.models.PasswordResetRequestModel;
import com.locadora.springboot.users.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/forgot")
    public ResponseEntity<String> processForgotPassword(@RequestBody EmailRequestModel emailRequestDTO) {
        String email = emailRequestDTO.getEmail();

        String token = userServices.createPasswordResetToken(email);
        if (token == null) {
            System.out.println("Token não gerado. Usuário não encontrado.");
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        String resetLink = "Digite este token para confirmar sua redefinição de senha: " + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Redefinição de senha");
        message.setText(resetLink);

        mailSender.send(message);

        return ResponseEntity.ok("Instruções de redefinição de senha enviadas para " + email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestModel passwordResetRequest) {
        String token = passwordResetRequest.getToken();
        String newPassword = passwordResetRequest.getNewPassword();

        boolean result = userServices.resetPassword(token, newPassword);
        if (result) {
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Falha ao redefinir a senha. Token inválido ou expirado.");
        }
    }

    @PostMapping("/reset-password/validate")
    public ResponseEntity<String> validateResetToken(@RequestBody TokenValidationRequest request) {
        String token = request.getToken();
        boolean isValid = userServices.validatePasswordResetToken(token);
        if (!isValid) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
        return ResponseEntity.ok("Token válido.");
    }
}