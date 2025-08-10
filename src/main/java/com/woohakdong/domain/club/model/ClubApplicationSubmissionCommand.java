package com.woohakdong.domain.club.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public record ClubApplicationSubmissionCommand(
        List<FormAnswer> formAnswers
) {
    public static ClubApplicationSubmissionCommand from(List<Map<String, Object>> answers) {
        List<FormAnswer> answerList = answers.stream()
                .map(answerMap -> new FormAnswer(
                        (Integer) answerMap.get("order"),
                        (String) answerMap.get("question"),
                        (Boolean) answerMap.get("required"),
                        answerMap.get("answer") // 단일 문자열 or 리스트
                ))
                .sorted(Comparator.comparing(FormAnswer::order))
                .toList();

        return new ClubApplicationSubmissionCommand(answerList);
    }
}
