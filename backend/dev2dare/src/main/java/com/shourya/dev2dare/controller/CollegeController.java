package com.shourya.dev2dare.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/college")
public class CollegeController {
    @PreAuthorize("hasAuthority('COLLEGE')")
    @PostMapping("/create-event")
    public String createEvent() {
        return "Event created! (Only colleges can access this endpoint)";
    }
} 