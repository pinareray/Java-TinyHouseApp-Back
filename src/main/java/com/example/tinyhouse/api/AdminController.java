package com.example.tinyhouse.api;

import com.example.tinyhouse.business.abstracts.AdminService;
import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.entities.dtos.AdminSummaryDto;
import com.example.tinyhouse.entities.dtos.SystemStatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DataResult<AdminSummaryDto>> getSystemSummary() {
        DataResult<AdminSummaryDto> result = adminService.getSystemSummary();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/statistics")
    public ResponseEntity<DataResult<SystemStatisticsDto>> getSystemStatistics() {
        DataResult<SystemStatisticsDto> result = adminService.getSystemStatistics();
        return ResponseEntity.ok(result);
    }
}
