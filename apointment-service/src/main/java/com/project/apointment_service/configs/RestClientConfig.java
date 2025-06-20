package com.project.apointment_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Bean
    RestClient.Builder restClientBuilder(){
        return RestClient.builder();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder
                .baseUrl("http://localhost:9091")
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(restClientAdapter).build();
    }

    @Bean
    public HealthProviderClient healthProviderClient(HttpServiceProxyFactory httpServiceProxyFactory){
        return httpServiceProxyFactory.createClient(HealthProviderClient.class);
    }
}
