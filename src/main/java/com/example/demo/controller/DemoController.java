package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class DemoController {

    @Autowired
    public RestTemplate restTemplate;

    @GetMapping("/hello")
    public ResponseEntity<String> getHelloWord() {
        final String newUrl = "https://localhost/";
        String response = restTemplate.getForObject(newUrl, String.class);

        return ResponseEntity.ok(response);
    }
}
