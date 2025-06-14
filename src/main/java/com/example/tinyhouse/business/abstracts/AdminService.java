package com.example.tinyhouse.business.abstracts;

import com.example.tinyhouse.core.utilities.results.DataResult;
import com.example.tinyhouse.entities.dtos.AdminSummaryDto;
import com.example.tinyhouse.entities.dtos.SystemStatisticsDto;

public interface AdminService {
    DataResult<AdminSummaryDto> getSystemSummary();
    DataResult<SystemStatisticsDto> getSystemStatistics();
}
