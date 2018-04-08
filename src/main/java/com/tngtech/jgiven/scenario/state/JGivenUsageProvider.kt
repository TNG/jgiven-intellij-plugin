package com.tngtech.jgiven.scenario.state

import com.intellij.psi.PsiField
import com.tngtech.jgiven.resolution.ResolutionHandler

class JGivenUsageProvider(
        private val scenarioStateProvider: ScenarioStateAnnotationProvider,
        private val resolutionHandler: ResolutionHandler,
        private val referenceFactory: ReferenceFactory
) {
    fun createReferenceIfJGivenUsage(fieldToSearch: PsiField, field: PsiField) =
            when {
                scenarioStateProvider.isJGivenScenarioState(field)
                        && fieldToSearch != field
                        && resolutionHandler.resolutionMatches(field, fieldToSearch) -> referenceFactory.referenceFor(field)
                else -> null
            }
}
