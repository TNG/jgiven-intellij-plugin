package com.tngtech.jgiven.reference

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiField
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider

class ReferenceProvider(
        private val scenarioStateProvider: ScenarioStateAnnotationProvider = ScenarioStateAnnotationProvider(),
        private val scenarioStateReferenceProvider: ScenarioStateReferenceProvider = ScenarioStateReferenceProvider()
) : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>() {

    override fun processQuery(queryParameters: ReferencesSearch.SearchParameters, consumer: Processor<in PsiReference>) {
        val element = queryParameters.elementToSearch

        ApplicationManager.getApplication().runReadAction {
            val scope = queryParameters.effectiveSearchScope
            if (scenarioStateProvider.isJGivenScenarioState(element) && scope is GlobalSearchScope) {
                val field = element as PsiField
                val references = scenarioStateReferenceProvider.findReferences(field)
                references.forEach { consumer.process(it) }
            }
        }
    }
}
