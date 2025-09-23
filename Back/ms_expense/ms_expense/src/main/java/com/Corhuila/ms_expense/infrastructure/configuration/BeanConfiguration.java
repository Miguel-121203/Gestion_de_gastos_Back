package com.Corhuila.ms_expense.infrastructure.configuration;

import com.Corhuila.ms_expense.domain.ports.ExpenseRepositoryPort;
import com.Corhuila.ms_expense.domain.ports.ExpenseServicePort;
import com.Corhuila.ms_expense.application.usecases.ExpenseUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ExpenseServicePort expenseServicePort(ExpenseRepositoryPort expenseRepositoryPort) {
        return new ExpenseUseCase(expenseRepositoryPort);
    }
}