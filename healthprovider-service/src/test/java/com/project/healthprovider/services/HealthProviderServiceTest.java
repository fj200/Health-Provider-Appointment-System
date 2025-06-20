package com.project.healthprovider.services;

import com.project.healthprovider.models.HealthProvider;
import com.project.healthprovider.respository.HealthProviderRepository;
import com.project.healthprovider.services.impl.HealthProviderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class HealthProviderServiceTest {

    @Mock
    private HealthProviderRepository healthProviderRepository;

    @InjectMocks
    private HealthProviderServiceImpl healthProviderService;

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

    @Test
    @Order(1)
    @DisplayName("Should return ID when saving a new health provider")
    public void shouldReturnId_whenHealthProviderIsSaved(){
        when(healthProviderRepository.save(any(HealthProvider.class))).thenReturn(healthProvider);

        String id = healthProviderService.createHealthProvider(healthProvider);

        verify(healthProviderRepository).save(healthProvider);
        assertEquals(healthProvider.getId(), id);
    }

    @Test
    @Order(2)
    @DisplayName("Should return List of health providers")
    public void shouldReturnHealthProviders(){
        when(healthProviderRepository.findProvidersByDateAndDepartment(any(), any())).thenReturn(List.of(healthProvider));

        List<HealthProvider> providers = healthProviderService.getAllAvailableHealthProviderByDateAndDepartment(LocalDate.now(), "");

        assertEquals(providers.get(0), healthProvider);

    }
}
