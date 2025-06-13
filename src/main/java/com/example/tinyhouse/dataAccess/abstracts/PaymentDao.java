package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Integer> {
}
