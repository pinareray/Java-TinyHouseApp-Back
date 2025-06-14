package com.example.tinyhouse.entities.dtos;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseListDto {
    private int id;
    private String title;
    private String description;
    private double price;
    private String location;
    private String status;
    private LocalDate availableFrom;
    private LocalDate availableTo;
}
