package com.project.apointment_service.services.impl;

import com.project.apointment_service.configs.HealthProviderClient;
import com.project.apointment_service.dtos.HealthProvider;
import com.project.apointment_service.dtos.HealthProviderResponse;
import com.project.apointment_service.models.Appointment;
import com.project.apointment_service.repositories.AppointmentRepository;
import com.project.apointment_service.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final HealthProviderClient healthProviderClient;
    private final AppointmentRepository appointmentRepository;

    @Override
    public String bookAppointment(LocalDate date, String hpDepartment) {
        List<HealthProvider> healthProviders = Optional
                .ofNullable(healthProviderClient.getAvailableHealthProviders(date, hpDepartment))
                .map(HealthProviderResponse::getProviders)
                .orElse(List.of());

        if (healthProviders.isEmpty()) {
            log.warn("No available health providers for date {} and department {}", date, hpDepartment);
            throw new IllegalStateException("No health providers available for the selected date and department.");
        }

        log.debug("Selected provider: {} for date {} and department {}", healthProviders.get(0).getHpName(), date, hpDepartment);

        Appointment appointment = Appointment
                .builder()
                .appointmentDate(date)
                .hpDepartment(hpDepartment)
                .hpName(healthProviders.get(0).getHpName())
                .build();
        appointment = appointmentRepository.save(appointment);

        log.info("AppointmentID {} successfully created", appointment.getId());
        return "Your appointment is confirmed";
    }
}
