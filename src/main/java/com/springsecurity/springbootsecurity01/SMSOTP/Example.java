package com.springsecurity.springbootsecurity01.SMSOTP;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;

public class Example {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC44e46488c35e0ea499db6338c799e502";
    public static final String AUTH_TOKEN = "633527d58103140a005b860d844617d8";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Verification verification = Verification.creator(
                        "VAa278b5ff806611614ce11237543bd29e",
                        "+8801517037513",
                        "sms")
                .create();

        System.out.println(verification.getSid());
    }
}