package com.tngtech.jgiven.resolution;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;

import javax.annotation.Nullable;
import java.util.Optional;

class ResolutionProvider {
    static final String FIELD_RESOLUTION = "resolution";
    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
    private AnnotationValueProvider annotationValueProvider = new AnnotationValueProvider();
    private TypeIsTooGenericCalculator typeIsTooGeneric = new TypeIsTooGenericCalculator();

    @Nullable
    Resolution getResolutionFrom(PsiField field) {
        PsiAnnotation annotation = scenarioStateProvider.getJGivenAnnotationOn(field);
        if (annotation == null) {
            return null;
        }

        PsiExpression annotationValue = annotationValueProvider.getAnnotationValue(annotation, FIELD_RESOLUTION);

        return Optional.ofNullable(annotationValue)
                .map(PsiElement::getText)
                .map(t -> {
                    for (Resolution resolution : Resolution.values()) {
                        if (resolution != Resolution.AUTO && t.contains(resolution.name())) {
                            return resolution;
                        }
                    }
                    return getResolutionForFieldType(field);
                }).orElse(getResolutionForFieldType(field));
    }

    /**
     * Calculate default resolution. See {{@link com.tngtech.jgiven.impl.inject.ScenarioStateField#getResolution()}}
     */
    private Resolution getResolutionForFieldType(PsiField field) {
        return typeIsTooGeneric.typeIsTooGeneric(field.getType())
                ? Resolution.NAME
                : Resolution.TYPE;
    }
}
