package com.project.healthprovider.controllers;

import com.project.healthprovider.models.HealthProvider;
import com.project.healthprovider.services.HealthProviderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/healthproviders")
public class HealthProviderController {
    private final HealthProviderService healthProviderService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createHealthProvider(@RequestBody @Valid HealthProvider healthProvider) {
        log.info("POST Health Provider called with payload: {}", healthProvider);
        String id = healthProviderService.createHealthProvider(healthProvider);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", id));
    }

    @GetMapping("/available")
    public ResponseEntity<Map<String, List<HealthProvider>>> getAllAvailableHealthProviderByDateAndDepartment(
            @RequestParam @NotNull(message = "Date of appointment cannot be null") LocalDate selectedDate,
            @RequestParam @NotBlank(message = "Department is required") String department
    ) {
        log.info("GET Available Health Provider at Date {} for department {}", selectedDate, department);
        List<HealthProvider> healthProviders = healthProviderService.getAllAvailableHealthProviderByDateAndDepartment(selectedDate, department);
        log.debug("Found health providers: {}", healthProviders);
        return ResponseEntity.ok(Map.of("providers", healthProviders));
    }
}
