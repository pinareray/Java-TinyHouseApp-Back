package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseDao extends JpaRepository<House, Integer> {
    List<House> findByStatus(String status); // Örn: Aktif ilanlar için
    List<House> findByHost_Id(int hostId);
}
