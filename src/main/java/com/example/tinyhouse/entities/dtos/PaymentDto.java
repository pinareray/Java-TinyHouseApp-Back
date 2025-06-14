package com.example.tinyhouse.entities.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private int id;
    private double amount;
    private LocalDateTime paymentDate;

    private ReservationDto reservation;
    private UserDto user;
}
