package com.example.tinyhouse.business.abstracts;

import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.ReservationCreateDto;
import com.example.tinyhouse.entities.dtos.ReservationDto;
import com.example.tinyhouse.entities.dtos.ReservationListDto;
import com.example.tinyhouse.entities.dtos.ReservationUpdateDto;

import java.util.List;

public interface ReservationService {
    DataResult<ReservationDto> add(ReservationCreateDto dto);
    DataResult<ReservationDto> update(ReservationUpdateDto dto);
    Result delete(int id, int requesterId);

    DataResult<ReservationDto> getById(int id, int requesterId);
    DataResult<List<ReservationListDto>> getAll(int requesterId);
    DataResult<List<ReservationListDto>> getByUserId(int userId, int requesterId);
    DataResult<List<ReservationListDto>> getByHouseId(int houseId, int requesterId);
}
