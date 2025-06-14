package com.example.tinyhouse.entities.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class SystemStatisticsDto {
    private Map<String, Integer> userCounts;         // Role - Count (e.g. ADMIN:10)
    private Map<String, Integer> monthlyReservations; // Ay - Rezervasyon sayısı
    private Map<String, Double> monthlyIncome;         // Ay - Gelir
}
