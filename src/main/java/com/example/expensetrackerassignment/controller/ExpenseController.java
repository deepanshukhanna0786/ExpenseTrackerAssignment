package com.example.expensetrackerassignment.controller;

import com.example.expensetrackerassignment.constants.ApplicationConstants;
import com.example.expensetrackerassignment.dto.ExpenseDto;
import com.example.expensetrackerassignment.dto.ResponseDto;
import com.example.expensetrackerassignment.exception.ExpenseNotFoundException;
import com.example.expensetrackerassignment.exception.InvalidExpenseDataException;
import com.example.expensetrackerassignment.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseDto<Page<ExpenseDto>> getAllExpensesPaginated(@RequestParam("startTime") Long startTime,
                                                                 @RequestParam("endTime") Long endTime,
                                                                 @RequestParam("page") Integer page,
                                                                 @RequestParam("maxLimit") Integer pageLimit) {
        Pageable pageable = Pageable.ofSize(pageLimit).withPage(page);
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "List of all expenses",
                expenseService.getAllExpensesPaginated(startTime, endTime, pageable),
                LocalDateTime.now(),
                "/api/expenses?startTime=" + startTime + "&endTime=" + endTime + "&page=" + page + "&maxLimit=" + pageLimit
        );
    }

    @GetMapping("/by-user-id/")
    public ResponseDto<Page<ExpenseDto>> getAllExpensesByUserIdPaginated(@RequestParam("startTime") Long startTime,
                                                                         @RequestParam("endTime") Long endTime,
                                                                         @RequestParam("page") Integer page,
                                                                         @RequestParam("maxLimit") Integer pageLimit,
                                                                         Principal principal) {
        String email = principal.getName();
        Pageable pageable = Pageable.ofSize(pageLimit).withPage(page);
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "List of all expenses by user ID",
                expenseService.getAllExpensesByUserIdPaginated(email, startTime, endTime, pageable),
                LocalDateTime.now(),
                "/api/expenses/my?startTime=" + startTime + "&endTime=" + endTime + "&page=" + page + "&maxLimit=" + pageLimit
        );
    }

    @PostMapping("")
    public ResponseDto<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseDto) {
        if (expenseDto.getAmount() <= 0) {
            throw new InvalidExpenseDataException("Expense amount must be greater than zero.");
        }
        ExpenseDto createdExpense = expenseService.createExpense(expenseDto);
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                201,
                "Expense created successfully",
                createdExpense,
                LocalDateTime.now(),
                "/api/expenses"
        );
    }

    @GetMapping("/{id}")
    public ResponseDto<ExpenseDto> getExpenseById(@PathVariable("id") Long id) {
        ExpenseDto expense = expenseService.getExpenseById(id);
        if (expense == null) {
            throw new ExpenseNotFoundException("Expense not found with ID: " + id);
        }
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "Expense retrieved successfully",
                expense,
                LocalDateTime.now(),
                "/api/expenses/" + id
        );
    }

    @PutMapping("/{id}")
    public ResponseDto<ExpenseDto> updateExpense(@PathVariable("id") Long id, @RequestBody ExpenseDto expenseDto) {
        if (expenseDto.getAmount() <= 0) {
            throw new InvalidExpenseDataException("Expense amount must be greater than zero.");
        }
        ExpenseDto updatedExpense = expenseService.updateExpense(id, expenseDto);
        if (updatedExpense == null) {
            throw new ExpenseNotFoundException("Expense not found with ID: " + id);
        }
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "Expense updated successfully",
                updatedExpense,
                LocalDateTime.now(),
                "/api/expenses/" + id
        );
    }

    @DeleteMapping("/{id}")
    public ResponseDto<String> deleteExpense(@PathVariable("id") Long id) {
        boolean isDeleted = expenseService.deleteExpense(id);
        if (!isDeleted) {
            throw new ExpenseNotFoundException("Expense not found with ID: " + id);
        }
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "Expense deleted successfully",
                null,
                LocalDateTime.now(),
                "/api/expenses/" + id
        );
    }


    @GetMapping("/total")
    public ResponseDto<Double> getTotalExpensesInDateRange(@RequestParam("startTime") Long startTime,
                                                           @RequestParam("endTime") Long endTime,
                                                           Principal principal) {
        String email = principal.getName();
        Double totalAmount = expenseService.getTotalExpensesInDateRange(email, startTime, endTime);
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "Total expenses calculated successfully",
                totalAmount,
                LocalDateTime.now(),
                "/api/expenses/total"
        );
    }


    @GetMapping("/category-summary")
    public ResponseDto<Map<String, Double>> getExpensesByCategory(@RequestParam("startTime") Long startTime,
                                                                  @RequestParam("endTime") Long endTime,
                                                                  Principal principal) {
        String email = principal.getName();
        Map<String, Double> categoryTotals = expenseService.getTotalExpensesByCategory(email, startTime, endTime);
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "Expenses summarized by category",
                categoryTotals,
                LocalDateTime.now(),
                "/api/expenses/category-summary"
        );
    }


    @GetMapping("/monthly-report")
    public ResponseDto<Map<String, Object>> getMonthlyExpenseReport(@RequestParam("year") int year,
                                                                    @RequestParam("month") int month,
                                                                    Principal principal) {
        String email = principal.getName();
        Map<String, Object> report = expenseService.generateMonthlyExpenseReport(email, year, month);
        return new ResponseDto<>(
                ApplicationConstants.SUCCESS,
                200,
                "Monthly Expense Report Generated",
                report,
                LocalDateTime.now(),
                "/api/expenses/monthly-report"
        );
    }
}
