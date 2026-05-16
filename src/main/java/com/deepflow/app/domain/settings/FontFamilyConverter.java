package com.deepflow.app.domain.settings;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FontFamilyConverter implements AttributeConverter<FontFamily, String> {

    private static final String LEGACY_SYSTEM = "system";

    @Override
    public String convertToDatabaseColumn(FontFamily attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public FontFamily convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank() || LEGACY_SYSTEM.equalsIgnoreCase(dbData)) {
            return FontFamily.NANUM_MYEONGJO;
        }
        return FontFamily.valueOf(dbData);
    }
}
