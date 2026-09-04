package com.tngtech.jgiven.usage.filter

import com.intellij.usages.ReadWriteAccessUsageInfo2UsageAdapter
import com.intellij.usages.Usage
import com.intellij.usages.rules.UsageFilteringRule
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider

class JGivenScenarioStateFilteringRule : UsageFilteringRule {
    private val scenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()

    override fun getRuleId() = RULE_ID

    override fun getActionId() = ACTION_ID

    override fun isVisible(usage: Usage): Boolean = !isJGivenScenarioStateUsage(usage)

    private fun isJGivenScenarioStateUsage(usage: Usage): Boolean =
            usage is ReadWriteAccessUsageInfo2UsageAdapter &&
                    scenarioStateAnnotationProvider.isJGivenScenarioState(usage.element)

    companion object {
        const val RULE_ID = "com.tngtech.jgiven.scenarioState"
        const val ACTION_ID = "UsageFiltering.JGiven"
    }
}
