package com.pricewatch.userAuth.service;

import com.pricewatch.userAuth.dto.UserResponse;
import com.pricewatch.userAuth.entity.User;
import com.pricewatch.userAuth.entity.UserPrincipal;
import com.pricewatch.userAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) return jwtService.generateToken(user.getUsername());

        return "Login Failed";
    }

    public String loadUser(UserPrincipal principal) {
        String userEmail = principal.getUserEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername()).toString();
    }
}

