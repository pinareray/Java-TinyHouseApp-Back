package com.example.tinyhouse.entities.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
    private int id;
    private String content;
    private int rating;
    private String userFullName; // yorum yapan kişinin adı soyadı
}
