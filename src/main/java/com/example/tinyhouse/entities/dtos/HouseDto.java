package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.concretes.House;
import lombok.*;

import java.time.LocalDate;

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

    public HouseDto(House house) {
        this.id = house.getId();
        this.title = house.getTitle();
        this.description = house.getDescription();
        this.price = house.getPrice();
        this.location = house.getLocation();
        this.status = house.getStatus();
        this.availableFrom = house.getAvailableFrom();
        this.availableTo = house.getAvailableTo();
    }
}
