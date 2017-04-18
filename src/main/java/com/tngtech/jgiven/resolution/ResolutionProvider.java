package com.tngtech.jgiven.resolution;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;

import javax.annotation.Nullable;
import java.util.Optional;

class ResolutionProvider {
    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();

    @Nullable
    Resolution getResolutionFrom(PsiField field) {
        PsiAnnotation annotation = scenarioStateProvider.getJGivenAnnotationOn(field);
        if (annotation == null) {
            return null;
        }

        PsiExpression annotationValue = AnnotationUtils.getAnnotationValue(annotation, "resolution");

        return Optional.ofNullable(annotationValue)
                .map(PsiElement::getText)
                .map(t -> {
                    for (Resolution resolution : Resolution.values()) {
                        if (t.contains(resolution.name())) {
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
        return typeIsTooGeneric(field.getType())
                ? Resolution.NAME
                : Resolution.TYPE;
    }

    private boolean typeIsTooGeneric(PsiType type) {
        PsiClass clazz = PsiTypesUtil.getPsiClass(type);
        if (clazz == null) {
            return false;
        }
        String qualifiedName = clazz.getQualifiedName();
        return qualifiedName == null
                || type instanceof PsiPrimitiveType
                || qualifiedName.startsWith("java.lang")
                || qualifiedName.startsWith("java.io")
                || qualifiedName.startsWith("java.util");
    }
}
