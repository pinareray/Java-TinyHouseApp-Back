package com.example.tinyhouse.entities.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentCreateDto {
    private double amount;
    private int reservationId;
    private int userId;
}

