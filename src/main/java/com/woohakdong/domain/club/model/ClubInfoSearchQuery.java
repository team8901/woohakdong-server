package com.woohakdong.domain.club.model;

public record ClubInfoSearchQuery(
        String name,
        String nameEn
) {
    public static ClubInfoSearchQuery of(String name, String nameEn) {
        return new ClubInfoSearchQuery(name, nameEn);
    }

    public boolean isEmpty() {
        return (name == null || name.isBlank()) && (nameEn == null || nameEn.isBlank());
    }
}
