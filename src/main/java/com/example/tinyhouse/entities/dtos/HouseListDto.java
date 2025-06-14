package com.example.tinyhouse.entities.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseListDto {
    private int id;
    private String title;
    private double price;
    private String location;
    private String status;
}
