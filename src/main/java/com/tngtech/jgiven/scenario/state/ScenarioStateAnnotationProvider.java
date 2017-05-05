package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierListOwner;
import com.tngtech.jgiven.Annotations;
import com.tngtech.jgiven.util.AnnotationProvider;

import javax.annotation.Nullable;
import java.util.Collection;

public class ScenarioStateAnnotationProvider {

    private AnnotationProvider annotationProvider = new AnnotationProvider();

    public boolean isJGivenScenarioState(PsiElement psiElement) {
        return isFieldAnnotatedWithAnyOf(psiElement, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES);
    }

    private boolean isFieldAnnotatedWithAnyOf(PsiElement psiElement, Collection<String> classNames) {
        return psiElement instanceof PsiField &&
                annotationProvider.findAnnotation((PsiModifierListOwner) psiElement, classNames) != null;
    }

    public boolean isProvidedScenarioState(PsiElement psiElement) {
        return isFieldAnnotatedWithAnyOf(psiElement, Annotations.PROVIDED_SCENARIO_STATE_CLASS_NAMES);
    }

    public boolean isExpectedScenarioState(PsiElement psiElement) {
        return isFieldAnnotatedWithAnyOf(psiElement, Annotations.EXPECTED_SCENARIO_STATE_CLASS_NAMES);
    }

    @Nullable
    @SuppressWarnings("ConstantConditions")
    public PsiAnnotation getJGivenAnnotationOn(PsiField field) {
        return annotationProvider.findAnnotation(field, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES);
    }
}
