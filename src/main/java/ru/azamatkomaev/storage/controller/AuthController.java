package ru.azamatkomaev.storage.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.azamatkomaev.storage.model.User;
import ru.azamatkomaev.storage.request.LoginRequest;
import ru.azamatkomaev.storage.request.RegisterRequest;
import ru.azamatkomaev.storage.response.LoginResponse;
import ru.azamatkomaev.storage.response.UserResponse;
import ru.azamatkomaev.storage.service.AuthService;
import ru.azamatkomaev.storage.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return ResponseEntity.ok(UserResponse.toUserResponse(user));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
        @RequestBody @Valid RegisterRequest request
    ) {
        User registeredUser = authService.register(request);
        return new ResponseEntity<>(UserResponse.toUserResponse(registeredUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @RequestBody @Valid LoginRequest request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.CREATED);
    }
}
