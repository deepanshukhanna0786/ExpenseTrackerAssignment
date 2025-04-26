package com.example.expensetrackerassignment.exception;

public class InvalidExpenseDataException extends RuntimeException {

    // Constructor to create an exception with a message
    public InvalidExpenseDataException(String message) {
        super(message);
    }
}
