package com.project.healthprovider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.healthprovider.controllers.HealthProviderController;
import com.project.healthprovider.models.HealthProvider;
import com.project.healthprovider.services.HealthProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthProviderController.class)
public class HealthProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HealthProviderService healthProviderService;

    @Autowired
    private ObjectMapper objectMapper;

    private HealthProvider healthProvider;

    @BeforeEach
    public void setUp(){
        healthProvider = HealthProvider
                .builder()
                .id("hp1")
                .hpName("Dr. Zooni")
                .hpDepartment("Gynecology")
                .isDailyAvailable(false)
                .daysAvailable(List.of(1L, 2L, 3L))
                .dutyStartTime(LocalTime.parse("10:00:00"))
                .dutyEndTime(LocalTime.parse("01:00:00"))
                .build();
    }

//    Post Controller
    @Test
    public void createHealthProvider() throws Exception{
        when(healthProviderService.createHealthProvider(any(HealthProvider.class))).thenReturn(healthProvider.getId());

        ResultActions response = mockMvc.perform(post("/api/v1/healthproviders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(healthProvider)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(healthProvider.getId())));
    }

    @Test
    public void shouldReturnProviders_whenGetAllAvailableHealthProviderByDateAndDepartment() throws Exception {
        when(healthProviderService.getAllAvailableHealthProviderByDateAndDepartment(any(), any())).thenReturn(List.of(healthProvider));

        mockMvc.perform(get("/api/v1/healthproviders/available")
                        .param("selectedDate", "2025-09-03")
                        .param("department", "Gynecology"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers").isArray())
                .andExpect(jsonPath("$.providers[0].id").value(healthProvider.getId()))
                .andExpect(jsonPath("$.providers[0].hpName").value(healthProvider.getHpName()))
                .andExpect(jsonPath("$.providers[0].hpDepartment").value(healthProvider.getHpDepartment()));
    }


    @Test
    public void serviceReturnsError() throws Exception{
        when(healthProviderService.createHealthProvider(any(HealthProvider.class)))
                .thenThrow(new RuntimeException("Service failure"));

        ResultActions response = mockMvc.perform(post("/api/v1/healthproviders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(healthProvider)));

        response.andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]", is("Service failure")));
    }

    @Test
    public void shouldReturn400_whenFieldValidationFails() throws Exception {
        HealthProvider invalidProvider = HealthProvider.builder()
                .hpName("")  // Empty name, assuming @NotBlank or similar validation exists
                .build();

        mockMvc.perform(post("/api/v1/healthproviders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProvider)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").exists());  // e.g. "hpName: must not be blank"
    }


}
