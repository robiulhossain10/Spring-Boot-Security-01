//package com.springsecurity.springbootsecurity01.security;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth/oauth2")
//public class OAuth2Controller {
//
//    @GetMapping("/success")
//    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal OAuth2User oauthUser) {
//        // Log all attributes for debugging
//        System.out.println("OAuth2User attributes: " + oauthUser.getAttributes());
//
//        String email = oauthUser.getAttribute("email");
//        String name = oauthUser.getAttribute("name");
//
//        // For Facebook, sometimes "name" or "email" might be missing depending on permissions
//        if (email == null) email = "Email not available";
//        if (name == null) name = "Name not available";
//
//        return ResponseEntity.ok("OAuth2 login successful! Email: " + email + ", Name: " + name);
//    }
//}
//
