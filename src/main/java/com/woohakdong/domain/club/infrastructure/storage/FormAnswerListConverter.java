package com.woohakdong.domain.club.infrastructure.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woohakdong.domain.club.model.FormAnswer;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class FormAnswerListConverter implements AttributeConverter<List<FormAnswer>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<FormAnswer> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing form content", e);
        }
    }

    @Override
    public List<FormAnswer> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<FormAnswer>>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Error deserializing form content", e);
        }
    }
}
