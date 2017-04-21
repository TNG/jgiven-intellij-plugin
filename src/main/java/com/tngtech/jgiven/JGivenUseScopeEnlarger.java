package com.tngtech.jgiven;

import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.UseScopeEnlarger;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JGivenUseScopeEnlarger extends UseScopeEnlarger {
    private ScenarioStateAnnotationProvider scenarioStateAnnotationProvider = new ScenarioStateAnnotationProvider();

    @Nullable
    @Override
    public SearchScope getAdditionalUseScope(@NotNull PsiElement element) {
        return scenarioStateAnnotationProvider.isJGivenScenarioState(element)
                ? GlobalSearchScope.everythingScope(element.getProject())
                : null;
    }
}
