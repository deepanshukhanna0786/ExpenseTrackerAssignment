package com.example.expensetrackerassignment.exception;

import lombok.Getter;

/**
 * Generic response format
 */
public class ResponseException extends RuntimeException {

    private final Integer appErrorCode;
    @Getter
    private final String errorMessage;

    private final Throwable cause;

    public ResponseException(Integer appErrorCode, String errorMessage) {
        super(errorMessage);
        this.appErrorCode = appErrorCode;
        this.errorMessage = errorMessage;
        this.cause = super.getCause();
    }

    public ResponseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.appErrorCode = 400;
        this.cause = super.getCause();
    }

    public ResponseException(int errorCode, String errorMessage, Throwable cause){
        super(errorMessage);
        this.appErrorCode = errorCode;
        this.errorMessage = errorMessage;
        this.cause = cause;
    }

    public Integer getErrorCode() {
        return appErrorCode;
    }

    @Override
    public synchronized Throwable getCause() {
        return cause == null? super.getCause(): cause;
    }
}
