package com.tngtech.jgiven.reference

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider

class JGivenUsageTypeProvider(
        private val scenarioStateAnnotationProvider: ScenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()
) : UsageTypeProvider {
    override fun getUsageType(element: PsiElement) =
            when {
                scenarioStateAnnotationProvider.isJGivenScenarioState(element) -> UsageType(USAGE_TYPE)
                else -> null
            }

    companion object {
        const val USAGE_TYPE = "JGiven Scenario State"
    }
}
