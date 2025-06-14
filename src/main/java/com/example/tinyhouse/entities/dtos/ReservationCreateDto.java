package com.example.tinyhouse.entities.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationCreateDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private int renterId;
    private int houseId;
}
