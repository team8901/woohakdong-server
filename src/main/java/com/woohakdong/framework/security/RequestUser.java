package com.woohakdong.framework.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class RequestUser implements UserDetails {
    private final Long userAuthId;
    private final List<GrantedAuthority> authorities;

    public RequestUser(Long userAuthId, List<GrantedAuthority> authorities) {
        this.userAuthId = userAuthId;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public Long getUserAuthId() {
        return userAuthId;
    }
}
