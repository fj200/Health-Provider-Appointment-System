package com.project.apointment_service.configs;

import com.project.apointment_service.dtos.HealthProvider;
import com.project.apointment_service.dtos.HealthProviderResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;
import java.util.List;

public interface HealthProviderClient {
    @GetExchange("/api/v1/healthproviders/available")
    HealthProviderResponse getAvailableHealthProviders(@RequestParam LocalDate selectedDate, @RequestParam String department);
}
