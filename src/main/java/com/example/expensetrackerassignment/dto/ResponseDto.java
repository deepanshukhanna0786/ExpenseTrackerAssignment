package com.example.expensetrackerassignment.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ResponseDto<T> {
    private String status;
    private Integer statusCode;
    private String message;
    private T response;
    private LocalDateTime timestamp;
    private String path;
}
