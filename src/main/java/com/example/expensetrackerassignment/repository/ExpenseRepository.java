package com.example.expensetrackerassignment.repository;

import com.example.expensetrackerassignment.entity.Expense;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    Page<Expense> findByTimestampBetween(Long startTime, Long endTime, Pageable pageable);

    Page<Expense> findByUserIdAndTimestampBetween(Long userId, Long startTime, Long endTime, Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.email = :email AND e.date BETWEEN :startDate AND :endDate")
    Double sumAmountByUserEmailAndDateRange(@Param("email") String email, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user.email = :email AND e.date BETWEEN :startDate AND :endDate GROUP BY e.category")
    List<Object[]> findSumAmountByCategory(@Param("email") String email, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    default Map<String, Double> sumAmountByCategory(String email, LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = findSumAmountByCategory(email, startDate, endDate);
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Object[] result : results) {
            categoryTotals.put((String) result[0], (Double) result[1]);
        }
        return categoryTotals;
    }

    List<Expense> findByUserEmailAndDateBetween(String email, LocalDateTime startDate, LocalDateTime endDate);




}
