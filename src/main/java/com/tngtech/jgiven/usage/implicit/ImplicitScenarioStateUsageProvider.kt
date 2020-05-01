package com.tngtech.jgiven.usage.implicit;

import com.intellij.codeInsight.daemon.ImplicitUsageProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;
import org.jetbrains.annotations.NotNull;

public class ImplicitScenarioStateUsageProvider implements ImplicitUsageProvider {
    private final ScenarioStateAnnotationProvider scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider();
    private final ScenarioStateReferenceProvider scenarioStateReferenceProvider = new ScenarioStateReferenceProvider();

    @Override
    public boolean isImplicitUsage(@NotNull PsiElement element) {
        return isImplicitRead(element) || isImplicitWrite(element);
    }

    @Override
    public boolean isImplicitRead(@NotNull PsiElement element) {
        return scenarioStateAnnotationProvider.isProvidedScenarioState(element)
                && hasAtLeastOneReference((PsiField) element);
    }

    @Override
    public boolean isImplicitWrite(@NotNull PsiElement element) {
        return scenarioStateAnnotationProvider.isExpectedScenarioState(element)
                && hasAtLeastOneReference((PsiField) element);
    }

    private boolean hasAtLeastOneReference(PsiField element) {
        return !scenarioStateReferenceProvider.findReferences(element, 1).isEmpty();
    }
}
