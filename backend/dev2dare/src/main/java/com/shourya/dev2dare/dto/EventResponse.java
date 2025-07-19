package com.shourya.dev2dare.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private String location;
    private String createdByEmail;
    private String status;
    private String registrationTimestamp; // ISO string, nullable, only for participation views
    private Boolean waitlisted; // nullable, only for participation views
}
