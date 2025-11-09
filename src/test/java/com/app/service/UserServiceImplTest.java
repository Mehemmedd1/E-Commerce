package com.app.service;

import com.app.dto.UserDto;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {


    @Test
    void findAll() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("test@test.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("tes2@test.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserDto> users = userService.findAll();
        assertEquals(2, users.size());
        assertEquals(user1.getId(), users.get(0).getId());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    void getMyProfile() {

    }
}