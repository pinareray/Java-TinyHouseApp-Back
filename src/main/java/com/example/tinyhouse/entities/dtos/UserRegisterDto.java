package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDto {

    @NotBlank(message = "İsim boş bırakılamaz.")
    @Size(max = 50, message = "İsim en fazla 50 karakter olabilir.")
    private String firstName;

    @NotBlank(message = "Soyisim boş bırakılamaz.")
    @Size(max = 50, message = "Soyisim en fazla 50 karakter olabilir.")
    private String lastName;

    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    @NotBlank(message = "Email boş bırakılamaz.")
    private String email;

    @NotBlank(message = "Şifre boş bırakılamaz.")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır.")
    private String password;

    private UserRole role;
}
