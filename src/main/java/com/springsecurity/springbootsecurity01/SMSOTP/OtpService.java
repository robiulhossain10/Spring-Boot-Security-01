package com.springsecurity.springbootsecurity01.SMSOTP;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber; // ✅ ঠিক import
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final TwilioConfig twilioConfig;
    private final Map<String, String> otpStorage = new HashMap<>();

    public OtpService(TwilioConfig config) {
        this.twilioConfig = config;
    }

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    public String sendOtp(String phone) {
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpStorage.put(phone, otp);

        Message.creator(
                new PhoneNumber(phone),                          // ✅ TO
                new PhoneNumber(twilioConfig.getTrialNumber()), // ✅ FROM (Twilio number)
                "Your OTP code is: " + otp
        ).create();

        return otp;
    }

    public boolean verifyOtp(String phone, String otp) {
        String storedOtp = otpStorage.get(phone);
        return storedOtp != null && storedOtp.equals(otp);
    }
}
