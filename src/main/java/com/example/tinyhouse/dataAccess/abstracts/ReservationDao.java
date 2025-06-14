package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.Reservation;
import com.example.tinyhouse.entities.enums.ReservationStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByRenter_Id(int renterId);
    List<Reservation> findByHouse_Id(int houseId);
    int countByStatus(ReservationStatus status);
    @Query("SELECT COUNT(r) FROM Reservation r WHERE MONTH(r.startDate) = :month")
    int countByMonth(@Param("month") int month);
    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.status = :status WHERE r.id = :id")
    int updateStatus(@Param("id") int id, @Param("status") ReservationStatus status);


    @Query(value = "SELECT dbo.fn_GetReservationCountByStatus(:status)", nativeQuery = true)
    Integer getReservationCountByStatusFn(@Param("status") String status);

    @Procedure(procedureName = "sp_GetMonthlyReservations")
    Integer getMonthlyReservations(
            @Param("year")  int year,
            @Param("month") int month
    );
}
