package com.praisomart.backend.auth.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendOtpSms(String phone, String otp, String purpose) {

        String message = "Praisomart OTP (" + purpose + "): " + otp +
                ". Valid for 5 minutes. Do not share.";

        System.out.println("SMS SENT TO " + phone + " : " + message);
    }
}