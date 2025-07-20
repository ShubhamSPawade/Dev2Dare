package com.shourya.dev2dare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
public class ProtectedTestController {
    private static final Logger logger = LoggerFactory.getLogger(ProtectedTestController.class);
    @GetMapping("/test")
    public String test() {
        logger.info("[ProtectedTestController] /protected/test endpoint called");
        String result = "You are authenticated!";
        logger.info("[ProtectedTestController] /protected/test endpoint returning: {}", result);
        return result;
    }
} 