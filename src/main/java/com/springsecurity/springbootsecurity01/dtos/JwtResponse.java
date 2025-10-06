package com.springsecurity.springbootsecurity01.dtos;



import java.time.LocalDateTime;
import java.util.Collection;

public record JwtResponse(
        String token,
        String username,
        String email,
        LocalDateTime dateCreated,
        LocalDateTime lastUpdated,
        Collection<String> roles
) {}