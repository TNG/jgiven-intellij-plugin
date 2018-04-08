package com.tngtech.jgiven.scenario.state

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifierListOwner
import com.tngtech.jgiven.Annotations
import com.tngtech.jgiven.util.AnnotationProvider

class ScenarioStateAnnotationProvider(
        private val annotationProvider: AnnotationProvider = AnnotationProvider()

) {

    fun isJGivenScenarioState(psiElement: PsiElement) =
            isFieldAnnotatedWithAnyOf(psiElement, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES)

    private fun isFieldAnnotatedWithAnyOf(psiElement: PsiElement, classNames: Collection<String>) =
            psiElement is PsiField && annotationProvider.findAnnotation(psiElement as PsiModifierListOwner, classNames) != null

    fun isProvidedScenarioState(psiElement: PsiElement) =
            isFieldAnnotatedWithAnyOf(psiElement, Annotations.PROVIDED_SCENARIO_STATE_CLASS_NAMES)

    fun isExpectedScenarioState(psiElement: PsiElement) =
            isFieldAnnotatedWithAnyOf(psiElement, Annotations.EXPECTED_SCENARIO_STATE_CLASS_NAMES)

    fun getJGivenAnnotationOn(field: PsiField) =
            annotationProvider.findAnnotation(field, Annotations.JGIVEN_SCENARIO_STATE_CLASS_NAMES)
}
