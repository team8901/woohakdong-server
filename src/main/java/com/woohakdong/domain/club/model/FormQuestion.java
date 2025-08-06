package com.woohakdong.domain.club.model;

import java.util.List;

public record FormQuestion(
        Integer order,
        String question,
        QuestionType type,
        Boolean required,
        List<String> options
) {
    enum QuestionType {
        TEXT,
        RADIO,
        CHECKBOX,
        SELECT;
    }
}
