package com.app.service;

import com.app.dto.UserDto;
import com.app.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto getMyProfile(User currentuser);

    String updateProfile(User currentUser, UserDto userDto);

    UserDto processOAuthPostLogin(String email, String name);

}
