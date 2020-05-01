package com.tngtech.jgiven.usage.implicit

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider

class ImplicitScenarioStateUsageProvider : ImplicitUsageProvider {
    private val scenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()
    private val scenarioStateReferenceProvider = ScenarioStateReferenceProvider()

    override fun isImplicitUsage(element: PsiElement) = isImplicitRead(element) || isImplicitWrite(element)

    override fun isImplicitRead(element: PsiElement) =
            (scenarioStateAnnotationProvider.isProvidedScenarioState(element)
                    && hasAtLeastOneReference(element as PsiField))

    override fun isImplicitWrite(element: PsiElement) =
            (scenarioStateAnnotationProvider.isExpectedScenarioState(element)
                    && hasAtLeastOneReference(element as PsiField))

    private fun hasAtLeastOneReference(element: PsiField) =
            !scenarioStateReferenceProvider.findReferences(element, 1).isEmpty()
}
