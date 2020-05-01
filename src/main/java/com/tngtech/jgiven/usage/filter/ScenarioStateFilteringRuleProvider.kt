package com.tngtech.jgiven.usage.filter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import com.intellij.usages.ConfigurableUsageTarget
import com.intellij.usages.UsageTarget
import com.intellij.usages.UsageView
import com.intellij.usages.impl.UsageViewImpl
import com.intellij.usages.rules.UsageFilteringRule
import com.intellij.usages.rules.UsageFilteringRuleProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider
import java.util.*

class ScenarioStateFilteringRuleProvider : UsageFilteringRuleProvider {
    private val scenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()

    override fun getActiveRules(project: Project): Array<UsageFilteringRule> {
        return emptyArray()
    }

    override fun createFilteringActions(view: UsageView): Array<AnAction> {
        return when {
            isNormalFindUsagesDialogAndNotShowUsages(view) -> arrayOf(
                    FilterByJGivenStateAction(scenarioStateAnnotationProvider, view)
            )
            else -> emptyArray()
        }
    }

    private fun isNormalFindUsagesDialogAndNotShowUsages(usageView: UsageView): Boolean {
        return canShowSettings(usageView)
    }

    private fun canShowSettings(usageView: UsageView): Boolean {
        if (usageView !is UsageViewImpl) {
            return false
        }
        return usageView.targets
                .any { it is ConfigurableUsageTarget }
    }
}
