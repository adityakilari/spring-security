package com.iralik.spring_security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class user {
    @GetMapping("/")
    public String test(HttpServletRequest request){
        return "Hello Sri.." + request.getSession().getId();
    }
}
