package com.records.Records.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Health OK";
    }
}
