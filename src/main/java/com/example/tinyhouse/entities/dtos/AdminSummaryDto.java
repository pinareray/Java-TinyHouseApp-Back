package com.example.tinyhouse.entities.dtos;

import com.example.tinyhouse.entities.concretes.House;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminSummaryDto {
    private int totalUsers;
    private int totalHosts;
    private int totalRenters;
    private int activeReservations;
    private int monthlyReservations;
    private double monthlyIncome;
}
