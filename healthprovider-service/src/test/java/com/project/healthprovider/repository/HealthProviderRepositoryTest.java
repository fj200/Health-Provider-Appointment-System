//package com.project.healthprovider.repository;
//
//import com.project.healthprovider.models.HealthProvider;
//import com.project.healthprovider.respository.HealthProviderRepository;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataMongoTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class HealthProviderRepositoryTest {
//
//    @Autowired
//    HealthProviderRepository healthProviderRepository;
//
//    @Test
//    @Order(1)
//    public void createHealthProvider() {
//        HealthProvider healthProvider = HealthProvider
//                .builder()
//                .id("hp1")
//                .hpName("Dr. Zooni")
//                .hpDepartment("Gynecology")
//                .isDailyAvailable(false)
//                .daysAvailable(List.of(1L, 2L, 3L))
//                .dutyStartTime(LocalTime.parse("10:00:00"))
//                .dutyEndTime(LocalTime.parse("10:00:00"))
//                .build();
//
//        healthProviderRepository.save(healthProvider);
//        assertEquals(1, healthProviderRepository.count());
//    }
//}
