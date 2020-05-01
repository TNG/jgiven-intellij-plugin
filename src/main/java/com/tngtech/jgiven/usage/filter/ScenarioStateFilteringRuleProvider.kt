package com.tngtech.jgiven.usage.filter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.usages.ConfigurableUsageTarget;
import com.intellij.usages.UsageView;
import com.intellij.usages.impl.UsageViewImpl;
import com.intellij.usages.rules.UsageFilteringRule;
import com.intellij.usages.rules.UsageFilteringRuleProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import org.jetbrains.annotations.NotNull;

import static java.util.Arrays.stream;

public class ScenarioStateFilteringRuleProvider implements UsageFilteringRuleProvider {
    private final ScenarioStateAnnotationProvider scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider();

    @NotNull
    @Override
    public UsageFilteringRule[] getActiveRules(@NotNull Project project) {
        return new UsageFilteringRule[0];
    }

    @NotNull
    @Override
    public AnAction[] createFilteringActions(@NotNull UsageView view) {
        if (isNormalFindUsagesDialogAndNotShowUsages(view)) {
            return new AnAction[]{
                    new FilterByJGivenStateAction(scenarioStateAnnotationProvider, view)
            };
        }
        return new AnAction[0];
    }

    private boolean isNormalFindUsagesDialogAndNotShowUsages(UsageView usageView) {
        return canShowSettings(usageView);
    }

    private boolean canShowSettings(UsageView usageView) {
        if (!(usageView instanceof UsageViewImpl)) {
            return false;
        }
        UsageViewImpl usageViewImpl = (UsageViewImpl) usageView;
        return stream(usageViewImpl.getTargets())
                .anyMatch(t -> t instanceof ConfigurableUsageTarget);
    }
}
