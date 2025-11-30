package com.pricewatch.userAuth.service;

import com.pricewatch.userAuth.entity.User;
import com.pricewatch.userAuth.entity.UserPrincipal;
import com.pricewatch.userAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user.get());
    }
}
