package com.shourya.dev2dare.utils;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {
    public String buildTemplate(String content) {
        return "<html><body>" + content + "</body></html>";
    }
} 