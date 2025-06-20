package com.project.apointment_service.controllers;

import com.project.apointment_service.services.AppointmentService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestParam @NotNull LocalDate selectedDate, @RequestParam @NotBlank String hpDepartment){
        appointmentService.bookAppointment(selectedDate, hpDepartment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
