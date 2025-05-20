package com.project.healthprovider.respository;

import com.project.healthprovider.models.HealthProvider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HealthProviderRepository extends MongoRepository<HealthProvider, Long> {

    @Query("{ 'hpDepartment': ?0, $or: [ { 'isDailyAvailable': true }, { 'daysAvailable': ?1, 'isDailyAvailable': false } ] }")
    List<HealthProvider> findProvidersByDateAndDepartment(String department, Long dayOfWeekValue);
}
