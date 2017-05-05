package com.tngtech.jgiven.usage.implicit;

import com.intellij.codeInsight.daemon.ImplicitUsageProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;

public class ImplicitScenarioStateUsageProvider implements ImplicitUsageProvider {
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider();
    private ScenarioStateReferenceProvider scenarioStateReferenceProvider = new ScenarioStateReferenceProvider();

    @Override
    public boolean isImplicitUsage(PsiElement element) {
        return isImplicitRead(element) || isImplicitWrite(element);
    }

    @Override
    public boolean isImplicitRead(PsiElement element) {
        return scenarioStateAnnotationProvider.isProvidedScenarioState(element)
                && hasAtLeastOneReference((PsiField) element);
    }

    @Override
    public boolean isImplicitWrite(PsiElement element) {
        return scenarioStateAnnotationProvider.isExpectedScenarioState(element)
                && hasAtLeastOneReference((PsiField) element);
    }

    private boolean hasAtLeastOneReference(PsiField element) {
        return !scenarioStateReferenceProvider.findReferences(element, 1).isEmpty();
    }
}
