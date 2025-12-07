package com.pricewatch.project.userAuth.controller;

import com.pricewatch.project.userAuth.entity.User;
import com.pricewatch.project.userAuth.entity.UserPrincipal;
import com.pricewatch.project.userAuth.service.JwtService;
import com.pricewatch.project.userAuth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user));
    }

    @PostMapping("/me")
    public ResponseEntity<String> me(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userService.loadUser(principal));
    }
}
