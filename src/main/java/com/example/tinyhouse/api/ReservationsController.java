package com.example.tinyhouse.api;

import com.example.tinyhouse.business.abstracts.ReservationService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.ReservationCreateDto;
import com.example.tinyhouse.entities.dtos.ReservationDto;
import com.example.tinyhouse.entities.dtos.ReservationListDto;
import com.example.tinyhouse.entities.dtos.ReservationUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationsController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Result add(@RequestBody ReservationCreateDto dto) {
        return reservationService.add(dto);
    }

    @PutMapping
    public Result update(@RequestBody ReservationUpdateDto dto) {
        return reservationService.update(dto);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id, @RequestParam int requesterId) {
        return reservationService.delete(id, requesterId);
    }

    @GetMapping("/{id}")
    public DataResult<ReservationDto> getById(@PathVariable int id, @RequestParam int requesterId) {
        return reservationService.getById(id, requesterId);
    }

    @GetMapping
    public DataResult<List<ReservationListDto>> getAll(@RequestParam int requesterId) {
        return reservationService.getAll(requesterId);
    }

    @GetMapping("/user/{userId}")
    public DataResult<List<ReservationListDto>> getByUserId(@PathVariable int userId, @RequestParam int requesterId) {
        return reservationService.getByUserId(userId, requesterId);
    }

    @GetMapping("/house/{houseId}")
    public DataResult<List<ReservationListDto>> getByHouseId(@PathVariable int houseId, @RequestParam int requesterId) {
        return reservationService.getByHouseId(houseId, requesterId);
    }
}
