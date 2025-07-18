package com.shourya.dev2dare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
public class ProtectedTestController {
    @GetMapping("/test")
    public String test() {
        return "You are authenticated!";
    }
} 