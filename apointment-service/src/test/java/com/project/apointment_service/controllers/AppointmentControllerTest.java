package com.project.apointment_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.apointment_service.services.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    public void shouldSucceed() throws Exception {

        LocalDate selectedDate = LocalDate.now();
        String hpDepartment = "Gynecology";
        when(
                appointmentService
                        .bookAppointment(selectedDate, hpDepartment))
                .thenReturn("");

        ResultActions response = mockMvc
                .perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("selectedDate", String.valueOf(selectedDate))
                        .param("hpDepartment", hpDepartment));

        verify(appointmentService).bookAppointment(any(LocalDate.class), any(String.class));

        response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

    }

    @Test
    public void shouldReturnBadRequest_whenIllegalStateException() throws Exception {
        LocalDate selectedDate = LocalDate.now();
        String hpDepartment = "Gynecology";
        String message = "No health providers available for the selected date and department.";
        when(
                appointmentService
                        .bookAppointment(selectedDate, hpDepartment))
                .thenAnswer((invocation) -> { throw new IllegalStateException(message);});

        ResultActions response = mockMvc
                .perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("selectedDate", String.valueOf(selectedDate))
                        .param("hpDepartment", hpDepartment));

        verify(appointmentService).bookAppointment(any(LocalDate.class), any(String.class));

        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]", is(message)));
    }

    @Test
    public void shouldReturnBadRequest_whenConstraintViolationException() throws Exception {
        ResultActions response = mockMvc
                .perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("selectedDate", String.valueOf(LocalDate.now()))
                        .param("hpDepartment", ""));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").exists());
    }


}
