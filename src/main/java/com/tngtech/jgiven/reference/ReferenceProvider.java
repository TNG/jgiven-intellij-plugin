package com.tngtech.jgiven.reference;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.tngtech.jgiven.scenario.state.ScenarioStateAnnotationProvider;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;
import org.jetbrains.annotations.NotNull;

public class ReferenceProvider extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
    private ScenarioStateAnnotationProvider scenarioStateProvider = new ScenarioStateAnnotationProvider();
    private ScenarioStateReferenceProvider scenarioStateReferenceProvider = new ScenarioStateReferenceProvider();

    @Override
    public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters, @NotNull Processor<PsiReference> consumer) {
        final PsiElement element = queryParameters.getElementToSearch();

        ApplicationManager.getApplication().runReadAction(() -> {
            SearchScope scope = queryParameters.getEffectiveSearchScope();
            if (!scenarioStateProvider.isJGivenScenarioState(element) || !(scope instanceof GlobalSearchScope)) {
                return;
            }
            PsiField field = (PsiField) element;
            scenarioStateReferenceProvider.findReferences(field).forEach(consumer::process);
        });
    }
}
