package com.app.controller;

import com.app.dto.UserDto;
import com.app.model.User;
import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")

public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("/profile")
    public ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal User currentuser) {
        return ResponseEntity.ok(userService.getMyProfile(currentuser));


    }
    @PutMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal User currentuser,
                                                @RequestBody UserDto userDto){
        String message=userService.updateProfile(currentuser,userDto);
        return ResponseEntity.ok(message);


    }
}

