package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.enums.UserRole;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserListDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private boolean isActive;
}

