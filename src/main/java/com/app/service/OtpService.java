package com.app.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class OtpService {
    public String generateOtp() {
        Random random=new SecureRandom();
        int otp= random.nextInt(1000,9999);
        return String.valueOf(otp);
    }
}
