package com.tngtech.jgiven.usage.filter

import com.intellij.openapi.project.Project
import com.intellij.usages.rules.UsageFilteringRule
import com.intellij.usages.rules.UsageFilteringRuleProvider

class ScenarioStateFilteringRuleProvider : UsageFilteringRuleProvider {
    override fun getApplicableRules(project: Project): Collection<UsageFilteringRule> =
            listOf(JGivenScenarioStateFilteringRule())
}
