package com.tngtech.jgiven.usage.filter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import com.intellij.usages.ConfigurableUsageTarget
import com.intellij.usages.UsageView
import com.intellij.usages.impl.UsageViewImpl
import com.intellij.usages.rules.UsageFilteringRule
import com.intellij.usages.rules.UsageFilteringRuleProvider
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider

class ScenarioStateFilteringRuleProvider : UsageFilteringRuleProvider {
    private val scenarioStateAnnotationProvider = ScenarioStateAnnotationProvider()

    @Deprecated("Use instead getApplicableRules", ReplaceWith("getApplicableRules()"))
    override fun getActiveRules(project: Project): Array<UsageFilteringRule> {
        return emptyArray()
    }

    @Deprecated("Use instead UsageFilteringRule.getActionId", ReplaceWith("UsageFilteringRule.getActionId()"))
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
