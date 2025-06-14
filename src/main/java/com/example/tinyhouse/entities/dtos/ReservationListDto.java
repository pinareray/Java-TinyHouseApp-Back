package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationListDto {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;

    private UserListDto renter;
    private HouseListDto house;
}
