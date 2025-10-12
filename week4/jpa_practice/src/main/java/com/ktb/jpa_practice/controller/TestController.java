package com.ktb.jpa_practice.controller;

import com.ktb.jpa_practice.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("GET request successful");
    }

    @PostMapping
    public ResponseEntity<String> postTest() {
        return ResponseEntity.ok("POST request successful");
    }

    @PutMapping
    public ResponseEntity<String> putTest() {
        return ResponseEntity.ok("PUT request successful");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTest() {
        System.out.println("DELETE /api/test 요청 수신");
        return ResponseEntity.ok("DELETE request successful");
    }
}