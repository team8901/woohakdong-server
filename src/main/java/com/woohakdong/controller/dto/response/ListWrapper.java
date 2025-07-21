package com.woohakdong.controller.dto.response;

import java.util.List;

public record ListWrapper<T>(
        List<T> data
) {
    public static <T> ListWrapper<T> of(List<T> data) {
        return new ListWrapper<>(data);
    }
}
