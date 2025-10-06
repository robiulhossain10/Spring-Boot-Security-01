//package com.springsecurity.springbootsecurity01.config;
//
//import io.jsonwebtoken.security.Keys;
//import javax.crypto.SecretKey;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JwtConfig {
//
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//
//    @Bean
//    public SecretKey jwtSigningKey() {
//        return Keys.hmacShaKeyFor(jwtSecret.getBytes()); // Important: use proper byte[]
//    }
//}
