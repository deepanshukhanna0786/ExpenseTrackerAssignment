package com.example.expensetrackerassignment.service.implementation;

import com.example.expensetrackerassignment.dto.ExpenseDto;
import com.example.expensetrackerassignment.entity.Expense;
import com.example.expensetrackerassignment.entity.User;
import com.example.expensetrackerassignment.mapper.ExpenseMapper;
import com.example.expensetrackerassignment.repository.ExpenseRepository;
import com.example.expensetrackerassignment.repository.UserRepository;
import com.example.expensetrackerassignment.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<ExpenseDto> getAllExpensesPaginated(Long startTime, Long endTime, Pageable pageable) {
        Page<Expense> expensesPage = expenseRepository.findByTimestampBetween(startTime, endTime, pageable);
        return expensesPage.map(ExpenseMapper::toDto);
    }

    @Override
    public Page<ExpenseDto> getAllExpensesByUserIdPaginated(String email, Long startTime, Long endTime, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Page<Expense> expensesPage = expenseRepository.findByUserIdAndTimestampBetween(user.getId(), startTime, endTime, pageable);
        return expensesPage.map(ExpenseMapper::toDto);
    }

    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        User user = userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = ExpenseMapper.toEntity(expenseDto, user);

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toDto(savedExpense);
    }


    @Override
    public ExpenseDto getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        return ExpenseMapper.toDto(expense);
    }

    @Override
    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        Expense existingExpense = expenseRepository.findById(id).orElse(null);
        if (existingExpense != null) {
            User user = userRepository.findById(expenseDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingExpense.setAmount(expenseDto.getAmount());
            existingExpense.setDescription(expenseDto.getDescription());
            existingExpense.setCategory(expenseDto.getCategory());
            existingExpense.setTimestamp(expenseDto.getTimestamp());
            existingExpense.setDate(expenseDto.getDate());
            existingExpense.setUser(user);

            Expense updatedExpense = expenseRepository.save(existingExpense);

            return ExpenseMapper.toDto(updatedExpense);
        }
        return null;
    }


    @Override
    public boolean deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Double getTotalExpensesInDateRange(String email, Long startTime, Long endTime) {
        LocalDateTime startDate = LocalDateTime.ofEpochSecond(startTime / 1000, 0, java.time.ZoneOffset.UTC);
        LocalDateTime endDate = LocalDateTime.ofEpochSecond(endTime / 1000, 0, java.time.ZoneOffset.UTC);
        return expenseRepository.sumAmountByUserEmailAndDateRange(email, startDate, endDate);
    }

    @Override
    public Map<String, Double> getTotalExpensesByCategory(String email, Long startTime, Long endTime) {
        LocalDateTime startDate = LocalDateTime.ofEpochSecond(startTime / 1000, 0, java.time.ZoneOffset.UTC);
        LocalDateTime endDate = LocalDateTime.ofEpochSecond(endTime / 1000, 0, java.time.ZoneOffset.UTC);
        return expenseRepository.sumAmountByCategory(email, startDate, endDate);
    }

    @Override
    public Map<String, Object> generateMonthlyExpenseReport(String email, int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        List<Expense> expenses = expenseRepository.findByUserEmailAndDateBetween(email, startOfMonth, endOfMonth);

        double totalExpense = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<String, Double> categoryBreakdown = new HashMap<>();
        for (Expense expense : expenses) {
            categoryBreakdown.put(
                    expense.getCategory(),
                    categoryBreakdown.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount()
            );
        }

        Map<String, Object> report = new HashMap<>();
        report.put("totalExpense", totalExpense);
        report.put("categoryBreakdown", categoryBreakdown);

        return report;
    }



}
