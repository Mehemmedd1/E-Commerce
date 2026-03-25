package com.app.auth.oauth2;

import com.app.dto.UserDto;
import com.app.security.jwt.JwtService;
import com.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    public CustomAuthenticationSuccessHandler(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email=oauth2User.getAttribute("email");
        String name=oauth2User.getAttribute("name");

        UserDto userDto= userService.processOAuthPostLogin(email,name);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDto.getEmail(),
                "",
                userDto.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );

        String token=jwtService.generateToken(userDetails);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);


        }

}
