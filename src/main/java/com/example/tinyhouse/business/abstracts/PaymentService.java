package com.example.tinyhouse.business.abstracts;

import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.MonthlyIncomeDto;
import com.example.tinyhouse.entities.dtos.PaymentCreateDto;
import com.example.tinyhouse.entities.dtos.PaymentDto;
import com.example.tinyhouse.entities.dtos.PaymentListDto;

import java.util.List;

public interface PaymentService {

    Result add(PaymentCreateDto dto);

    Result delete(int id, int requesterId);

    DataResult<PaymentDto> getById(int id, int requesterId);

    DataResult<List<PaymentListDto>> getAll(int requesterId);

    DataResult<List<PaymentListDto>> getByUserId(int userId, int requesterId);

    DataResult<List<PaymentListDto>> getByReservationId(int reservationId, int requesterId);

    DataResult<List<MonthlyIncomeDto>> getMonthlyIncomeByHostId(int hostId);
}
