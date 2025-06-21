package com.project.apointment_service.services;

import com.project.apointment_service.configs.HealthProviderClient;
import com.project.apointment_service.dtos.HealthProvider;
import com.project.apointment_service.dtos.HealthProviderResponse;
import com.project.apointment_service.models.Appointment;
import com.project.apointment_service.repositories.AppointmentRepository;
import com.project.apointment_service.services.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {
    @Mock
    private HealthProviderClient healthProviderClient;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private static HealthProviderResponse response;

    private static String department;

    private static LocalDate selectedDate;

    private static Appointment appointment;

    @BeforeAll
    public static void setUp(){
        HealthProvider provider = HealthProvider
                .builder()
                .id("hp1")
                .hpName("Dr. Zooni")
                .hpDepartment("Gynecology")
                .isDailyAvailable(false)
                .daysAvailable(List.of(1L, 2L, 3L))
                .dutyStartTime(LocalTime.parse("10:00:00"))
                .dutyEndTime(LocalTime.parse("01:00:00"))
                .build();

        response = new HealthProviderResponse(List.of(provider));
        selectedDate = LocalDate.now();
        department = "Gynecology";
        appointment = Appointment
                .builder()
                .id(1L)
                .appointmentDate(selectedDate)
                .hpDepartment(department)
                .hpName(provider.getHpName())
                .build();
    }

    @Test
    public void shouldSucceed(){
        when(
                healthProviderClient
                .getAvailableHealthProviders(any(LocalDate.class), any(String.class)))
                .thenReturn(response);

        when(appointmentRepository
                .save(any(Appointment.class)))
                .thenReturn(appointment);

        String result = appointmentService.bookAppointment(LocalDate.now(), "Gynecology");

        verify(healthProviderClient).getAvailableHealthProviders(selectedDate, department);

        assertEquals("Your appointment is confirmed",result);

    }

    @Test()
    public void shouldThrowIllegalStateException_WhenNoProvider(){
        response.setProviders(List.of());
        when(
                healthProviderClient
                        .getAvailableHealthProviders(any(LocalDate.class), any(String.class)))
                .thenReturn(response);

        Throwable exception = assertThrows(IllegalStateException.class,
                () -> appointmentService.bookAppointment(LocalDate.now(), "Gynecology"));

        verify(healthProviderClient).getAvailableHealthProviders(selectedDate, department);

        assertEquals("No health providers available for the selected date and department.", exception.getMessage());
    }

}
