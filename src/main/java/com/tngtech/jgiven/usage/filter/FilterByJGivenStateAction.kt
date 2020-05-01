package com.tngtech.jgiven.usage.filter

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.usages.ReadWriteAccessUsageInfo2UsageAdapter
import com.intellij.usages.Usage
import com.intellij.usages.UsageView
import com.intellij.usages.rules.UsageFilteringRuleProvider
import com.tngtech.jgiven.Icons.JGIVEN
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

internal class FilterByJGivenStateAction(private val scenarioStateAnnotationProvider: ScenarioStateAnnotationProvider, private val usageView: UsageView) : ToggleAction("JGiven", "Filter by JGiven Scenario State", JGIVEN) {
    private var excludedUsages: Set<Usage> = HashSet()
    override fun isSelected(e: AnActionEvent): Boolean {
        return JGivenSettings.instance.isJGivenFilteringEnabled
    }

    override fun setSelected(event: AnActionEvent, state: Boolean) {
        JGivenSettings.instance.isJGivenFilteringEnabled = state
        val project = event.project ?: return
        val scenarioStateUsages = usageView.usages
                .filter { usage: Usage? ->
                    usage is ReadWriteAccessUsageInfo2UsageAdapter &&
                            scenarioStateAnnotationProvider.isJGivenScenarioState(usage.element)
                }
                .toSet()
        if (state) {
            excludedUsages.stream()
                    .filter { u: Usage -> !scenarioStateUsages.contains(u) }
                    .forEach { usage: Usage? -> usageView.appendUsage(usage!!) }
        } else {
            excludedUsages = usageView.usages.stream()
                    .filter { o: Usage -> scenarioStateUsages.contains(o) }
                    .collect(Collectors.toSet())
            excludedUsages.forEach(Consumer { usage: Usage? -> usageView.removeUsage(usage!!) })
        }
        project.messageBus.syncPublisher(UsageFilteringRuleProvider.RULES_CHANGED).run()
    }

}
