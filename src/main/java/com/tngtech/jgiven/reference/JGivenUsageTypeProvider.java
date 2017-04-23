package com.tngtech.jgiven.reference;

import com.intellij.psi.PsiElement;
import com.intellij.usages.impl.rules.UsageType;
import com.intellij.usages.impl.rules.UsageTypeProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import org.jetbrains.annotations.Nullable;

public class JGivenUsageTypeProvider implements UsageTypeProvider {
    static final String USAGE_TYPE = "JGiven Scenario State";
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider();

    @Nullable
    @Override
    public UsageType getUsageType(PsiElement element) {
        if (scenarioStateAnnotationProvider.isJGivenScenarioState(element)) {
            return new UsageType(USAGE_TYPE);
        }
        return null;
    }
}
