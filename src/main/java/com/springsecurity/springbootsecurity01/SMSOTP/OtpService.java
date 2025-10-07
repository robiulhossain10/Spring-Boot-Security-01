package com.springsecurity.springbootsecurity01.SMSOTP;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class OtpService {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.service_sid}")
    private String serviceSid;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public String sendOtp(String phone) {
        String formattedPhone = phone.startsWith("+") ? phone : "+" + phone;

        Verification verification = Verification.creator(
                serviceSid,
                formattedPhone,
                "sms"
        ).create();

        return "OTP sent to " + formattedPhone;
    }
}
