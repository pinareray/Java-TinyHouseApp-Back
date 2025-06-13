package com.example.tinyhouse.entities.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginDto {

    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    @NotBlank(message = "Email boş bırakılamaz.")
    private String email;

    @NotBlank(message = "Şifre boş bırakılamaz.")
    private String password;
}