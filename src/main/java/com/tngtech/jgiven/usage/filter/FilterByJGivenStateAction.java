package com.tngtech.jgiven.usage.filter;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.usages.ReadWriteAccessUsageInfo2UsageAdapter;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageView;
import com.intellij.usages.rules.UsageFilteringRuleProvider;
import com.tngtech.jgiven.Icons;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class FilterByJGivenStateAction extends ToggleAction {
    private boolean state = false;
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider;
    private UsageView usageView;
    private Set<Usage> excludedUsages = new HashSet<>();

    FilterByJGivenStateAction(ScenarioStateAnnotationProvider scenarioStateAnnotationProvider, UsageView usageView) {
        super("JGiven", "Filter by JGiven Scenario State", Icons.JGIVEN);
        this.scenarioStateAnnotationProvider = scenarioStateAnnotationProvider;
        this.usageView = usageView;

    }

    @Override
    public boolean isSelected(AnActionEvent e) {
        return state;
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        this.state = state;
        if (e.getProject() == null) {
            return;
        }
        Set<Usage> scenarioStateUsages = usageView.getUsages().stream()
                .filter(u -> u instanceof ReadWriteAccessUsageInfo2UsageAdapter &&
                        scenarioStateAnnotationProvider.isJGivenScenarioState(((ReadWriteAccessUsageInfo2UsageAdapter) u).getElement()))
                .collect(Collectors.toSet());

        if (state) {
            excludedUsages = usageView.getUsages().stream()
                    .filter(u -> !scenarioStateUsages.contains(u))
                    .collect(Collectors.toSet());
            excludedUsages.forEach(usageView::removeUsage);
        } else {
            excludedUsages.stream()
                    .filter(u -> !scenarioStateUsages.contains(u))
                    .forEach(usageView::appendUsage);
        }
        e.getProject().getMessageBus().syncPublisher(UsageFilteringRuleProvider.RULES_CHANGED).run();
    }
}
