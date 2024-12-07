package com.example.jwt.controller;

import com.example.jwt.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {


        return ResponseEntity.ok(loginService.login(email, password));
    }

}