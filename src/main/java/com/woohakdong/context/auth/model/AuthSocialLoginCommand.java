package com.woohakdong.context.auth.model;

public record AuthSocialLoginCommand (
        String provider,
        String providerAccessToken
){
}
