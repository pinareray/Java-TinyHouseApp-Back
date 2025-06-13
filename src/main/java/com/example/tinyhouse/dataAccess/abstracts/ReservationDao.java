package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByRenter_Id(int renterId);
}
