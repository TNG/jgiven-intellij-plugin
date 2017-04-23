package com.tngtech.jgiven.scenario.state;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierListOwner;
import com.tngtech.jgiven.Annotations;
import com.tngtech.jgiven.util.AnnotationProvider;

import javax.annotation.Nullable;

public class ScenarioStateAnnotationProvider {

    private AnnotationProvider annotationProvider = new AnnotationProvider();

    public boolean isJGivenScenarioState(PsiElement psiElement) {
        return psiElement instanceof PsiField &&
                annotationProvider.findAnnotation((PsiModifierListOwner) psiElement,
                        Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES) != null;
    }

    @Nullable
    @SuppressWarnings("ConstantConditions")
    public PsiAnnotation getJGivenAnnotationOn(PsiField field) {
        return annotationProvider.findAnnotation(field, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES);
    }
}
