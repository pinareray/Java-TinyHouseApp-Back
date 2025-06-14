package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseDao extends JpaRepository<House, Integer> {
    /**
     * Belirtilen duruma (örneğin: Aktif, Pasif) sahip ilanları döndürür.
     */
    List<House> findByStatus(String status);

    /**
     * Belirli bir ev sahibine (host) ait tüm ilanları döndürür.
     */
    List<House> findByHost_Id(int hostId);

    /**
     * Belirli bir ev sahibine ait, belirtilen duruma sahip ilanları döndürür.
     * (Örn: host_id=3 ve status="Aktif" olanlar)
     */
    List<House> findByHost_IdAndStatus(int hostId, String status);

    /**
     * Başlığa göre arama yapar (partial match).
     */
    List<House> findByTitleContainingIgnoreCase(String keyword);
}
