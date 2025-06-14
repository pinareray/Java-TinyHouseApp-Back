package com.example.tinyhouse.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDto {
    private int id;
    private String content;
    private int rating;
    private int userId;
    private String userFullName;
    private int houseId;
    private String houseTitle;
}


