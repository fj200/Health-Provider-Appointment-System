package com.project.apointment_service.services;

import java.time.LocalDate;

public interface AppointmentService {
    String bookAppointment(LocalDate date, String hpDepartment);
}
