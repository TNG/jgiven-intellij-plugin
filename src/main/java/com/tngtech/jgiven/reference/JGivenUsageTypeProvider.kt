package com.tngtech.jgiven.reference

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider

class JGivenUsageTypeProvider(
        private val scenarioStateAnnotationProvider: ScenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()
) : UsageTypeProvider {
    val usageType = UsageType {"JGiven Scenario State"}
    override fun getUsageType(element: PsiElement) =
            when {
                scenarioStateAnnotationProvider.isJGivenScenarioState(element) -> usageType
                else -> null
            }
}
