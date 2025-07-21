package com.woohakdong.controller.dto.response;

import java.util.List;

public record ListWrapper<T>(
        List<T> result
) {
    public static <T> ListWrapper<T> of(List<T> data) {
        return new ListWrapper<>(data);
    }
}
