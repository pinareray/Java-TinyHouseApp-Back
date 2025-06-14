package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Integer> {
    List<Payment> findByUser_Id(int userId);
    Optional<Payment> findByReservation_Id(int reservationId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE FUNCTION('MONTH', p.paymentDate) = :month AND FUNCTION('YEAR', p.paymentDate) = :year")
    Double sumIncomeByMonth(@Param("month") int month, @Param("year") int year);

    @Procedure(procedureName = "sp_GetMonthlyIncome")
    BigDecimal getMonthlyIncome(
            @Param("year")  int year,
            @Param("month") int month
    );
}
