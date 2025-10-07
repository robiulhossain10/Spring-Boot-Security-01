package com.springsecurity.springbootsecurity01.SMSOTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @GetMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String phone) {
        String result = otpService.sendOtp(phone);
        return ResponseEntity.ok(result);
    }
}
