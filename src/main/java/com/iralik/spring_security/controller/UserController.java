package com.iralik.spring_security.controller;

import com.iralik.spring_security.dto.AppUserInfo;
import com.iralik.spring_security.serviceImp.AppUserImp;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final AppUserImp appUserImp;

    public UserController( AppUserImp appUserImp) {
        this.appUserImp = appUserImp;
    }
    @GetMapping("/")
    public String test(HttpServletRequest request){
        return "Hello Sri.." + request.getSession().getId();
    }

    /**
     * Handles user registration.
     * This method accepts a user registration request, attempts to save the user,
     * and returns an appropriate response.
     *
     * @param user The user information received in the request body.
     * @return A {@link ResponseEntity} containing the saved user data if successful,
     *         or an error message if registration fails.
     *
     * @throws RuntimeException If a user with the same ID already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody AppUserInfo user) {
        try {
            AppUserInfo savedUser = appUserImp.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (RuntimeException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", ex.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "An unexpected error occurred.");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Handles user login.
     * This method accepts login credentials, verifies the user, and returns a JWT token if authentication is successful.
     *
     * @param user The login credentials provided in the request body.
     * @return A JWT token if authentication is successful, or an error message if authentication fails.
     */
    @PostMapping("/login")
    public String logIn(@RequestBody AppUserInfo user){
        return appUserImp.verify(user);
    }
}
