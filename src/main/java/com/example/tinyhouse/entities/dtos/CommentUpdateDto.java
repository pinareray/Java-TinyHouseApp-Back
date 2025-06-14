package com.example.tinyhouse.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// CommentUpdateDto.java
@Data
public class CommentUpdateDto {
    private int id;
    private String content;
    private int rating;
    private int userId; // yetki kontrolü için
}

