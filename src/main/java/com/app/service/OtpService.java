package com.app.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class OtpService {
    public String generateOtp() {
        Random random=new SecureRandom();
        int otp= 1000+random.nextInt(9000);
        return String.valueOf(otp);
    }
}
