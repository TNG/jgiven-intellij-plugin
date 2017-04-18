package com.tngtech.jgiven.scenario.state;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierListOwner;
import com.tngtech.jgiven.Annotations;

import javax.annotation.Nullable;
import java.util.Objects;

public class ScenarioStateAnnotationProvider {

    public boolean isJGivenScenarioState(PsiElement psiElement) {

        return psiElement instanceof PsiField &&
                AnnotationUtil.findAnnotation((PsiModifierListOwner) psiElement,
                        Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES) != null;
    }

    @Nullable
    @SuppressWarnings("ConstantConditions")
    public PsiAnnotation getJGivenAnnotationOn(PsiField field) {
        return Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES.stream()
                .map(a -> AnnotationUtil.findAnnotation(field, a))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
