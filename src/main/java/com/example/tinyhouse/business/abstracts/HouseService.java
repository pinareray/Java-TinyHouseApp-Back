package com.example.tinyhouse.business.abstracts;

import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.HouseCreateDto;
import com.example.tinyhouse.entities.dtos.HouseDto;
import com.example.tinyhouse.entities.dtos.HouseListDto;
import com.example.tinyhouse.entities.dtos.HouseUpdateDto;

import java.util.List;

public interface HouseService {

    // Tüm ilanları listele - kim istiyor?
    DataResult<List<HouseListDto>> getAll(int requesterId);

    // Aktif ilanları listele - kim istiyor?
    DataResult<List<HouseListDto>> getActiveHouses(int requesterId);

    // Belirli bir ilanı getir - kim istiyor?
    DataResult<HouseDto> getById(int houseId, int requesterId);

    // Yeni ilan ekle - DTO içinde requesterId olmalı
    DataResult<HouseDto> add(HouseCreateDto houseDto);

    // Güncelleme - DTO içinde requesterId olmalı
    DataResult<HouseDto> update(HouseUpdateDto houseDto);

    // Silme - kim siliyor?
    Result delete(int houseId, int requesterId);

    // Belirli bir ev sahibine ait ilanları getir - kim istiyor?
    DataResult<List<HouseListDto>> getByHostId(int hostId, int requesterId);
}
