package com.example.expensetrackerassignment.service;

import com.example.expensetrackerassignment.dto.ExpenseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Map;

public interface ExpenseService {

    Page<ExpenseDto> getAllExpensesPaginated(Long startTime, Long endTime, Pageable pageable);

    Page<ExpenseDto> getAllExpensesByUserIdPaginated(String email, Long startTime, Long endTime, Pageable pageable);

    ExpenseDto createExpense(ExpenseDto expenseDto);

    ExpenseDto getExpenseById(Long id);

    ExpenseDto updateExpense(Long id, ExpenseDto expenseDto);

    boolean deleteExpense(Long id);

    Double getTotalExpensesInDateRange(String email, Long startTime, Long endTime);

    Map<String, Double> getTotalExpensesByCategory(String email, Long startTime, Long endTime);

    Map<String, Object> generateMonthlyExpenseReport(String email, int year, int month);



}
