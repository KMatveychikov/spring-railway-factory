package ru.matvey.springrailwayfactory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springrailwayfactory.model.dto.auth.AuthRequest;
import ru.matvey.springrailwayfactory.model.dto.auth.AuthResponse;
import ru.matvey.springrailwayfactory.model.dto.auth.RegisterRequest;
import ru.matvey.springrailwayfactory.model.dto.auth.UserResponse;
import ru.matvey.springrailwayfactory.services.AuthService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/get_all")
    public List<UserResponse> getAllUsers(){
       return service.getAllUsersResponse();
    }

}
