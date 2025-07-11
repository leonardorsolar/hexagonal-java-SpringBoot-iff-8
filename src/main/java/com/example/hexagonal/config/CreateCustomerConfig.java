package com.example.hexagonal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hexagonal.application.usecase.CreateCustomerUseCase;
import com.example.hexagonal.infrastructure.adapter.output.MongoCustomerRepositoryAdapter;
import com.example.hexagonal.infrastructure.adapter.output.client.ViaCepAddressAdapter;

@Configuration
public class CreateCustomerConfig {

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(
            ViaCepAddressAdapter viaCepAddressAdapter,
            MongoCustomerRepositoryAdapter mongoCustomerRepositoryAdapter) {
        return new CreateCustomerUseCase(viaCepAddressAdapter, mongoCustomerRepositoryAdapter);
    }

}
