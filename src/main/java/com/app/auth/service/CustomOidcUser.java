package com.app.auth.service;

import com.app.auth.dto.CustomOAuth2User;
import com.app.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

public class CustomOidcUser extends CustomOAuth2User implements OidcUser {

    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;

    public CustomOidcUser(User user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(user, attributes);
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    @Override
    public Map<String, Object> getClaims() {
        return getAttributes();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }
}
