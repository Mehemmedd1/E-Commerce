package com.app.service;

import com.app.dto.UserDto;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


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
        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("email@com");
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);
        UserDto userDto = userService.getMyProfile(user);
        assertEquals(user.getId(), userDto.getId());
    }
}