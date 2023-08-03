package co.com.perficient.project3.service.impl;

import co.com.perficient.project3.config.JwtUtils;
import co.com.perficient.project3.model.dto.SignInRequest;
import co.com.perficient.project3.model.entity.UserP3;
import co.com.perficient.project3.repository.UserRepository;
import co.com.perficient.project3.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public String signUp(UserP3 user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(Collections.emptySet());
        userRepository.save(user);
        return jwtUtils.generateToken(userDetailsService.createUser(user));
    }

    @Override
    public String signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserP3 user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username() + "not found"));
        return jwtUtils.generateToken(userDetailsService.createUser(user));
    }
}
