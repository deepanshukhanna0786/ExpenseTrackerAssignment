package com.example.expensetrackerassignment.mapper;

import com.example.expensetrackerassignment.dto.ExpenseDto;
import com.example.expensetrackerassignment.entity.Expense;
import com.example.expensetrackerassignment.entity.User;

public class ExpenseMapper {

    // Map Expense entity to ExpenseDto
    public static ExpenseDto toDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setCategory(expense.getCategory());
        dto.setDate(expense.getDate());
        dto.setUserId(expense.getUser().getId()); // userId from user object
        dto.setTimestamp(expense.getTimestamp());
        return dto;
    }

    // Map ExpenseDto to Expense entity
    public static Expense toEntity(ExpenseDto dto, User user) {
        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setCategory(dto.getCategory());
        expense.setDate(dto.getDate());
        expense.setUser(user);
        expense.setTimestamp(dto.getTimestamp());
        return expense;
    }
}
