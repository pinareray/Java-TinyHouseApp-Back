package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;

// CommentCreateDto.java
@Data
public class CommentCreateDto {
    private String content;
    private int rating;
    private int userId;
    private int houseId;
}

