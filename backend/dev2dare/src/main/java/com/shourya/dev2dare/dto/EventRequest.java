package com.shourya.dev2dare.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotNull
    private LocalDateTime eventDateTime;

    @NotBlank
    private String location;
}
