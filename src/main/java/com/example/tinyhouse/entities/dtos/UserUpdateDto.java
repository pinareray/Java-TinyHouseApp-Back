package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    @NotNull(message = "Kullanıcı ID boş olamaz.")
    private int id;

    @NotBlank(message = "İsim boş bırakılamaz.")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Soyisim boş bırakılamaz.")
    @Size(max = 50)
    private String lastName;

    @Email(message = "Geçerli bir e-posta girin.")
    @NotBlank(message = "Email boş bırakılamaz.")
    private String email;

    private UserRole role;
}
