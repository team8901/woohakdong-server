package com.woohakdong.framework.security;

import com.woohakdong.domain.auth.model.UserAuthRole;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class RequestUser implements UserDetails {
    private final Long userAuthId;
    private final UserAuthRole userAuthRole;

    public RequestUser(Long userAuthId, UserAuthRole userAuthRole) {
        this.userAuthId = userAuthId;
        this.userAuthRole = userAuthRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userAuthRole.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
