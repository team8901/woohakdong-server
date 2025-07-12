package com.woohakdong.domain.auth.model;

public record AuthSocialLoginCommand (
        String provider,
        String providerAccessToken
){
}
