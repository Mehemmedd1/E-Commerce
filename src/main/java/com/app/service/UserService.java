package com.app.service;

import com.app.dto.UserDto;
import com.app.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface UserService {
   List<UserDto> findAll();
   UserDto getMyProfile( User currentuser);
   String updateProfile(User currentUser, UserDto userDto);

}
