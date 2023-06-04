package com.ascending.training.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = {"/healthcheck"})
public class HealthCheckController {

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity healthCheck() {
        return ResponseEntity.status(HttpServletResponse.SC_OK).body("This is a health check controller");
    }
}
