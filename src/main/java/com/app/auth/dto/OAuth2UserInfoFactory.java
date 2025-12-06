package com.app.auth.dto;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("Dəstəklənməyən OAuth2 provayderi: " + registrationId);
    }
}