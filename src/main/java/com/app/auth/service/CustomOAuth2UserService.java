package com.app.auth.service;

import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String,Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user=userOptional.get();
        }else {
            user=new User();
            user.setEmail(email);
            user.setName(name);
            user.setEnabled(true);
            Role userRole=roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("User Rolu tapılmadı"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
        return (OAuth2User) user;



    }

}
