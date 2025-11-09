package com.app.auth.service;

import com.app.auth.dto.AuthResponse;
import com.app.auth.dto.LoginRequest;
import com.app.auth.dto.RegisterRequest;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.security.jwt.JwtService;
import com.app.service.EmailService;
import com.app.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpService otpService;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Bu e-mail artıq qeydiyyatdan keçib.");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("USER rolu tapılmadı. Zəhmət olmasa DB-də yaradın."));

        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(userRole));
        user.setEnabled(false);
        String otp = otpService.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(3));

        User savedUser = userRepository.save(user);
        emailService.sendOtpMail(user.getEmail(), "Qeydiyyat Təstiqləmə kodu", otp);



        return "Qeydiyyat uğurludur zəhmət olmasa e poçta göndərilən kodu təstiqləyin";

    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Login Xətası: İstifadəçi tapılmadı."));

        var jwtToken = jwtService.generateToken(user);

        String roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(","));

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .role(roles)
                .build();
    }

    public AuthResponse verifyRegistration(String email, String otp) {
        User user=userRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("Istifadəçi tapılmadı"));
        if (user.getOtpCode()==null||!user.getOtpCode().equals(otp)){
            throw new RuntimeException("Wrong OTP code");
        }
        if (user.getOtpExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP code expired");
        }
        user.setEnabled(true);
        user.setOtpCode(null);
        user.setOtpExpiryDate(null);
        User savedUser = userRepository.save(user);
        String jwtToken=jwtService.generateToken(savedUser);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .role(RoleName.ROLE_USER.name())
                .build();
    }
    public String forgotPassword(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("Istifadəçi tapılmadı"));
        String otp=otpService.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);
        emailService.sendOtpMail(email,"Parolu yeniləmək üçün kod",otp);
        return "Email adresinə kod göndərildi";
    }
    public String resetPassword(String email,String otp,String newPassword){
        User user=userRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("Istifadəçi tapılmadı"));
        if (user.getOtpCode()==null||!user.getOtpCode().equals(otp)){
            throw new RuntimeException("Wrong OTP code");
        }
        if (user.getOtpExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP code expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtpCode(null);
        user.setOtpExpiryDate(null);
        userRepository.save(user);
        return "Parol yenisi ilə əvəz olundu";
    }


}