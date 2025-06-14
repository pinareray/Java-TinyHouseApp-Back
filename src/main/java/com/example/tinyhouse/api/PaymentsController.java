package com.example.tinyhouse.api;

import com.example.tinyhouse.business.abstracts.PaymentService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.MonthlyIncomeDto;
import com.example.tinyhouse.entities.dtos.PaymentCreateDto;
import com.example.tinyhouse.entities.dtos.PaymentDto;
import com.example.tinyhouse.entities.dtos.PaymentListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Result add(@RequestBody PaymentCreateDto dto) {
        return paymentService.add(dto);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id, @RequestParam int requesterId) {
        return paymentService.delete(id, requesterId);
    }

    @GetMapping("/{id}")
    public DataResult<PaymentDto> getById(@PathVariable int id, @RequestParam int requesterId) {
        return paymentService.getById(id, requesterId);
    }

    @GetMapping
    public DataResult<List<PaymentListDto>> getAll(@RequestParam int requesterId) {
        return paymentService.getAll(requesterId);
    }

    @GetMapping("/user/{userId}")
    public DataResult<List<PaymentListDto>> getByUserId(@PathVariable int userId, @RequestParam int requesterId) {
        return paymentService.getByUserId(userId, requesterId);
    }

    @GetMapping("/reservation/{reservationId}")
    public DataResult<List<PaymentListDto>> getByReservationId(@PathVariable int reservationId, @RequestParam int requesterId) {
        return paymentService.getByReservationId(reservationId, requesterId);
    }

    @GetMapping("/monthly-income/{hostId}")
    public DataResult<List<MonthlyIncomeDto>> getMonthlyIncome(@PathVariable int hostId) {
        return paymentService.getMonthlyIncomeByHostId(hostId);
    }
}
