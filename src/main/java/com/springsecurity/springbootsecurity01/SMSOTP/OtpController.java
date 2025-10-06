package com.springsecurity.springbootsecurity01.SMSOTP;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String phone) {
        // Validate phone parameter (optional)
        if (phone == null || phone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }

        String otp = otpService.sendOtp(phone);
        return ResponseEntity.ok("OTP sent to " + phone);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String phone, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(phone, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }
}
