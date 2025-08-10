package com.woohakdong.domain.club.model;

public record FormAnswer (
        Integer order,
        String question,
        Boolean required,
        Object answer // 단일 문자열 or 리스트 → 후처리에서 형 변환 필요
){
}
