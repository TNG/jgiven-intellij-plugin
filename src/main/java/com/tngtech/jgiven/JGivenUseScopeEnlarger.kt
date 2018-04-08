package com.tngtech.jgiven

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.UseScopeEnlarger
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider

class JGivenUseScopeEnlarger : UseScopeEnlarger() {
    private val scenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()

    override fun getAdditionalUseScope(element: PsiElement): SearchScope? {
        return when {
            scenarioStateAnnotationProvider.isJGivenScenarioState(element) -> GlobalSearchScope.everythingScope(element.project)
            else -> null
        }
    }
}
