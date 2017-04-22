package com.tngtech.jgiven.scenario.state;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.usages.ReadWriteAccessUsageInfo2UsageAdapter;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageView;
import com.intellij.usages.rules.UsageFilteringRule;
import com.intellij.usages.rules.UsageFilteringRuleProvider;
import com.tngtech.jgiven.Icons;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ScenarioStateFilteringRuleProvider implements UsageFilteringRuleProvider {
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider();

    @NotNull
    @Override
    public UsageFilteringRule[] getActiveRules(@NotNull Project project) {
        return new UsageFilteringRule[0];
    }

    @NotNull
    @Override
    public AnAction[] createFilteringActions(@NotNull UsageView view) {
        return new AnAction[]{
                new FilterByJGivenStateAction(scenarioStateAnnotationProvider, view)
        };
    }

    private static class FilterByJGivenStateAction extends ToggleAction {
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
            e.getProject().getMessageBus().syncPublisher(RULES_CHANGED).run();
        }
    }
}
