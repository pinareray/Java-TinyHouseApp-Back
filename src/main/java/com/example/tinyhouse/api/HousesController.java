package com.example.tinyhouse.api;

import com.example.tinyhouse.business.abstracts.HouseService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.core.utilities.results.Result;
import com.example.tinyhouse.entities.dtos.HouseCreateDto;
import com.example.tinyhouse.entities.dtos.HouseDto;
import com.example.tinyhouse.entities.dtos.HouseListDto;
import com.example.tinyhouse.entities.dtos.HouseUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin
public class HousesController {

    private final HouseService houseService;

    @Autowired
    public HousesController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<HouseListDto>>> getAll(@RequestParam int requesterId) {
        return ResponseEntity.ok(houseService.getAll(requesterId));
    }

    @GetMapping("/getActiveHouses")
    public ResponseEntity<DataResult<List<HouseListDto>>> getActiveHouses(@RequestParam int requesterId) {
        return ResponseEntity.ok(houseService.getActiveHouses(requesterId));
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<HouseDto>> getById(@RequestParam int id, @RequestParam int requesterId) {
        return ResponseEntity.ok(houseService.getById(id, requesterId));
    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<HouseDto>> add(@Valid @RequestBody HouseCreateDto dto) {
        return ResponseEntity.ok(houseService.add(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<DataResult<HouseDto>> update(@Valid @RequestBody HouseUpdateDto dto) {
        return ResponseEntity.ok(houseService.update(dto));
    }

    @DeleteMapping("/deletebyid")
    public ResponseEntity<Result> deleteById(@RequestParam int id, @RequestParam int requesterId) {
        return ResponseEntity.ok(houseService.delete(id, requesterId));
    }

    @GetMapping("/getbyhostid")
    public ResponseEntity<DataResult<List<HouseListDto>>> getByHostId(@RequestParam int hostId, @RequestParam int requesterId) {
        return ResponseEntity.ok(houseService.getByHostId(hostId, requesterId));
    }
}
