package com.woohakdong.domain.util.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageResourceType {
    CLUB_PROFILE("club-profile"),
    USER_PROFILE("user-profile"),
    CLUB_BANNER("club-banner"),
    ;

    private final String value;
}
