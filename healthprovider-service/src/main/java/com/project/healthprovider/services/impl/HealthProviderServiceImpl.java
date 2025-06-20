package com.project.healthprovider.services.impl;

import com.project.healthprovider.models.HealthProvider;
import com.project.healthprovider.respository.HealthProviderRepository;
import com.project.healthprovider.services.HealthProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthProviderServiceImpl implements HealthProviderService {

    private final HealthProviderRepository healthProviderRepository;

    @Override
    public String createHealthProvider(HealthProvider healthProvider) {
        String id =  healthProviderRepository.save(healthProvider).getId();
        log.info("Health Provider {} created", id);
        return id;
    }

    @Override
    public List<HealthProvider> getAllAvailableHealthProviderByDateAndDepartment(LocalDate selectedDate, String department) {
        // Convert LocalDate to DayOfWeek
        DayOfWeek selectedDayOfWeek = selectedDate.getDayOfWeek();
        // Convert DayOfWeek to its numerical value (1 = Monday, 2 = Tuesday, ..., 7 = Sunday)
        Long dayOfWeekValue = (long) selectedDayOfWeek.getValue();

        log.debug("Fetching providers - Date: {}, Department: {}", selectedDate, department);
        // Call repository method with converted day of the week
        List<HealthProvider> healthProviders = healthProviderRepository.findProvidersByDateAndDepartment(department, dayOfWeekValue);

        log.info("Found {} providers for {} on {}", healthProviders.size(), department, selectedDate);
        return healthProviders;
    }
}
