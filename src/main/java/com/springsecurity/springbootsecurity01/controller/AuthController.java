package com.springsecurity.springbootsecurity01.controller;


import com.springsecurity.springbootsecurity01.dtos.JwtResponse;
import com.springsecurity.springbootsecurity01.dtos.LoginRequest;
import com.springsecurity.springbootsecurity01.dtos.RegisterRequest;
import com.springsecurity.springbootsecurity01.entity.Role;
import com.springsecurity.springbootsecurity01.entity.User;
import com.springsecurity.springbootsecurity01.repository.RoleRepository;
import com.springsecurity.springbootsecurity01.repository.UserRepository;
import com.springsecurity.springbootsecurity01.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(loginRequest.username());

            User user = userRepository.findByUserName(loginRequest.username())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Collection<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    user.getUserName(),
                    user.getEmail(),
                    roles
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUserName(registerRequest.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.email())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setUserName(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEmail(registerRequest.email());
        user.setUserFirstName(registerRequest.firstName());
        user.setUserLastName(registerRequest.lastName());

        // Set default values
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);

        // Set roles
        Set<Role> roles = new HashSet<>();
        if (registerRequest.roles() == null || registerRequest.roles().isEmpty()) {
            // Default role
            Role userRole = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
            roles.add(userRole);
        } else {
            registerRequest.roles().forEach(role -> {
                Role foundRole = roleRepository.findByRoleName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role " + role + " not found."));
                roles.add(foundRole);
            });
        }
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/init-roles")
    public ResponseEntity<?> initRoles() {
        try {
            // Create default roles if they don't exist
            String[] defaultRoles = {"USER", "ADMIN", "MODERATOR"};
            for (String roleName : defaultRoles) {
                if (!roleRepository.existsById(roleName)) {
                    Role role = new Role();
                    role.setRoleName(roleName);
                    role.setRoleDescription(roleName + " Role");
                    roleRepository.save(role);
                }
            }
            return ResponseEntity.ok("Roles initialized successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error initializing roles: " + e.getMessage());
        }
    }
}