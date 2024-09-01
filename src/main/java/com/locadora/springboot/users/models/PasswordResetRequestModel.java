package com.locadora.springboot.users.models;

import lombok.*;

@Getter
@Setter
public class PasswordResetRequestModel {
    private String token;
    private String newPassword;
}
