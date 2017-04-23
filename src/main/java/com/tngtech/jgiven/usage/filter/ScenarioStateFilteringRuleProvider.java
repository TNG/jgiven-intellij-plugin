package com.tngtech.jgiven.usage.filter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.usages.UsageView;
import com.intellij.usages.rules.UsageFilteringRule;
import com.intellij.usages.rules.UsageFilteringRuleProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import org.jetbrains.annotations.NotNull;

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

}
