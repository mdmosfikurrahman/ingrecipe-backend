package org.epde.ingrecipe.recipe.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RecipeStatusConverter implements AttributeConverter<RecipeStatus, Long> {

    @Override
    public Long convertToDatabaseColumn(RecipeStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public RecipeStatus convertToEntityAttribute(Long value) {
        if (value == null) {
            return null;
        }
        return RecipeStatus.fromValue(value);
    }
}
