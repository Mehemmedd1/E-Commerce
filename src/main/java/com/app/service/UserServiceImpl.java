package com.app.service;

import com.app.dto.UserDto;
import com.app.model.Orders;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private UserDto convertToDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        List<Orders> orders = user.getOrders().stream().toList();
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .orders(orders)
                .build();
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDto = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return userDto;
    }

    @Override
    public UserDto getMyProfile(User currentuser) {
        return convertToDto(currentuser);
    }

    @Override
    public String updateProfile(User currentUser, UserDto userDto) {
        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bu email artıq istifadə olunur!");
        }
        if (userDto.getName() != null) {
            currentUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            currentUser.setEmail(userDto.getEmail());
        }

        userRepository.save(currentUser);
        return "Profile with id: " + currentUser.getId() + " updated successfully";
    }

    @Override
    public UserDto processOAuthPostLogin(String email, String name) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);

                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("User role is not found"));
                    newUser.setRoles(Set.of(userRole));

                    newUser.setOrders(new ArrayList<>());

                    return convertToDto(userRepository.save(newUser));
                });
    }
}
