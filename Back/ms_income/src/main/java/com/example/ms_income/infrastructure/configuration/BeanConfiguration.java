package com.example.ms_income.infrastructure.configuration;

import com.example.ms_income.domain.ports.IncomeRepositoryPort;
import com.example.ms_income.domain.ports.IncomeServicePort;
import com.example.ms_income.application.usecases.IncomeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IncomeServicePort incomeServicePort(IncomeRepositoryPort incomeRepositoryPort) {
        return new IncomeUseCase(incomeRepositoryPort);
    }
}
