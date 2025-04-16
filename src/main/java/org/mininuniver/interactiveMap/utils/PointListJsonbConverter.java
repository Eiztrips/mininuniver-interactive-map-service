package org.mininuniver.interactiveMap.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mininuniver.interactiveMap.models.submodels.Point;

import java.io.IOException;
import java.util.List;

@Converter
public class PointListJsonbConverter implements AttributeConverter<List<Point>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Point> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting Map to JSON string", e);
        }
    }

    @Override
    public List<Point> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Point>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON string to Map", e);
        }
    }
}