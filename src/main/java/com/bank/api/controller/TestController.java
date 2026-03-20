package com.bank.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

//    http://localhost:9090/api/test/ping
    @GetMapping("/ping")
    public String ping() {
        return "Wallet API is Online!";
    }
}
