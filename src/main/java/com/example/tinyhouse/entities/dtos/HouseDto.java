package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.concretes.House;
import com.example.tinyhouse.entities.concretes.User;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseDto {
    private int id;
    private String title;
    private String description;
    private double price;
    private String location;
    private String status;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    private int commentCount;
    private double averageRating;
    private List<CommentDto> comments; // yorum listesi eklendi
    private UserDto host;

    public HouseDto(House house) {
        this.id = house.getId();
        this.title = house.getTitle();
        this.description = house.getDescription();
        this.price = house.getPrice();
        this.location = house.getLocation();
        this.status = house.getStatus();
        this.availableFrom = house.getAvailableFrom();
        this.availableTo = house.getAvailableTo();
        // commentCount ve averageRating dışarıdan manuel set edilmeli
    }
}
