package com.example.tinyhouse.entities.dtos;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseCreateDto {
    private int requesterId;
    private String title;
    private String description;
    private double price;
    private String location;
    private String status;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private int hostId;
}
