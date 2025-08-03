package com.woohakdong.domain.club.model;

import java.util.List;

public record ClubApplicationFormCreateCommand(
        String name,
        List<FormQuestion> formContent
) {
}
