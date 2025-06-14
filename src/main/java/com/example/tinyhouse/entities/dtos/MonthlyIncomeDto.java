package com.example.tinyhouse.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyIncomeDto {
    private String month;     // Örn: "Ocak"
    private double totalIncome; // Örn: 4200.00
}