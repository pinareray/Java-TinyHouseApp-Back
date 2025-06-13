package com.example.tinyhouse.entities.concretes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "status")
    private String status; // paid / unpaid

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
